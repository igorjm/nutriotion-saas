package br.com.nutritionplatform.platform.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

class JwtSecurityConfigurationTest {
    @Test
    void acceptsOnlyTheConfiguredAudience() {
        var validator = JwtSecurityConfiguration.audienceValidator("authenticated");

        assertThat(validator.validate(jwt(List.of("authenticated"), "aal1")).hasErrors()).isFalse();
        assertThat(validator.validate(jwt(List.of("another-api"), "aal1")).hasErrors()).isTrue();
    }

    @Test
    void grantsClinicalAssuranceAuthorityOnlyForAal2() {
        var converter = new JwtSecurityConfiguration().supabaseJwtAuthenticationConverter();

        AbstractAuthenticationToken aal1 = converter.convert(jwt(List.of("authenticated"), "aal1"));
        AbstractAuthenticationToken aal2 = converter.convert(jwt(List.of("authenticated"), "aal2"));

        assertThat(aal1).isNotNull();
        assertThat(aal1.getAuthorities()).extracting("authority").doesNotContain("AAL2");
        assertThat(aal2).isNotNull();
        assertThat(aal2.getAuthorities()).extracting("authority").contains("AAL2");
    }

    @Test
    void requiresAnExplicitAlgorithmForAnOverriddenJwkSet() {
        var configuration = new JwtSecurityConfiguration();

        assertThatThrownBy(() -> configuration.jwtDecoder(
                        "https://issuer.example.invalid",
                        "https://keys.example.invalid/jwks.json",
                        "",
                        "authenticated"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("jws-algorithm");
    }

    @Test
    void buildsAnEs256DecoderForTheLocalJwkSet() {
        var configuration = new JwtSecurityConfiguration();

        assertThat(configuration.jwtDecoder(
                        "http://127.0.0.1:54321/auth/v1",
                        "http://host.docker.internal:54321/auth/v1/.well-known/jwks.json",
                        "ES256",
                        "authenticated"))
                .isNotNull();
    }

    private Jwt jwt(List<String> audience, String assuranceLevel) {
        Instant issuedAt = Instant.parse("2026-08-25T12:00:00Z");
        return Jwt.withTokenValue("fictional-token")
                .header("alg", "RS256")
                .subject("00000000-0000-4000-8000-000000000001")
                .audience(audience)
                .claim("aal", assuranceLevel)
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(3600))
                .build();
    }
}
