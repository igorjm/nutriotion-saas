package br.com.nutritionplatform.platform.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

@Configuration
@Profile("!dev & !test")
class JwtSecurityConfiguration {
    private static final String AAL2_AUTHORITY = "AAL2";

    @Bean
    JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}") String jwkSetUri,
            @Value("${app.security.jws-algorithm:}") String jwsAlgorithm,
            @Value("${app.security.oidc-audience}") String audience) {
        NimbusJwtDecoder decoder;
        if (StringUtils.hasText(jwkSetUri)) {
            if (!StringUtils.hasText(jwsAlgorithm)) {
                throw new IllegalArgumentException(
                        "app.security.jws-algorithm is required when a JWK Set URI is configured");
            }
            var builder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri);
            builder.jwsAlgorithm(SignatureAlgorithm.from(jwsAlgorithm));
            decoder = builder.build();
        } else {
            decoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);
        }
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(issuerUri),
                audienceValidator(audience)));
        return decoder;
    }

    @Bean
    SecurityFilterChain jwtSecurityFilterChain(
            HttpSecurity http,
            @Value("${app.security.require-aal2:true}") boolean requireAal2,
            Converter<Jwt, ? extends AbstractAuthenticationToken> supabaseJwtAuthenticationConverter)
            throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(
                                    "/api/v1/public/**",
                                    "/actuator/health/**",
                                    "/v3/api-docs/**")
                            .permitAll();
                    if (requireAal2) {
                        authorize.requestMatchers("/api/v1/patients/**")
                                .hasAuthority(AAL2_AUTHORITY);
                    }
                    authorize.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(supabaseJwtAuthenticationConverter)))
                .build();
    }

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> supabaseJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> granted = scopes.convert(jwt);
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            if (granted != null) authorities.addAll(granted);
            if ("aal2".equals(jwt.getClaimAsString("aal"))) {
                authorities.add(new SimpleGrantedAuthority(AAL2_AUTHORITY));
            }
            return authorities;
        });
        return converter;
    }

    static OAuth2TokenValidator<Jwt> audienceValidator(String requiredAudience) {
        return jwt -> {
            if (jwt.getAudience().contains(requiredAudience)) {
                return OAuth2TokenValidatorResult.success();
            }
            OAuth2Error error = new OAuth2Error(
                    "invalid_token",
                    "The required token audience is missing.",
                    null);
            return OAuth2TokenValidatorResult.failure(error);
        };
    }
}
