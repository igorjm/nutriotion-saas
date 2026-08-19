package br.com.nutritionplatform.identity;

import java.util.UUID;

public record SessionContext(
        UUID userId,
        String displayName,
        UUID organizationId,
        String organizationName,
        String role) {
}
