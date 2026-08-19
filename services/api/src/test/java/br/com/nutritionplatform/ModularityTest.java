package br.com.nutritionplatform;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTest {
    @Test
    void modulesRespectDeclaredDependencies() {
        ApplicationModules.of(NutritionPlatformApplication.class).verify();
    }
}
