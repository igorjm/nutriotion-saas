package br.com.nutritionplatform.identity;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class IdentityContextService {
    private final JdbcClient jdbc;

    public IdentityContextService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public SessionContext resolve(String externalSubject) {
        return jdbc.sql("""
                SELECT
                    u.id AS user_id,
                    u.display_name,
                    o.id AS organization_id,
                    o.name AS organization_name,
                    m.role
                FROM app_user u
                JOIN membership m ON m.user_id = u.id
                JOIN organization o ON o.id = m.organization_id
                WHERE u.external_subject = :externalSubject
                  AND m.status = 'ACTIVE'
                  AND m.is_default = TRUE
                """)
                .param("externalSubject", externalSubject)
                .query((rs, rowNum) -> new SessionContext(
                        rs.getObject("user_id", java.util.UUID.class),
                        rs.getString("display_name"),
                        rs.getObject("organization_id", java.util.UUID.class),
                        rs.getString("organization_name"),
                        rs.getString("role")))
                .optional()
                .orElseThrow(MembershipNotFoundException::new);
    }
}
