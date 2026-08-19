package br.com.nutritionplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulith;

@Modulith(systemName = "Nutrition Platform")
@SpringBootApplication
public class NutritionPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(NutritionPlatformApplication.class, args);
    }
}
