package com.vetcare.calculation_service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test class for Calculation Service Application.
 * 
 * This test ensures that the Spring application context
 * loads successfully without any configuration issues.
 */
@SpringBootTest
class CalculationServiceApplicationTests {

    @Test
    @DisplayName("Application context should load successfully")
    void contextLoads() {
        // This test will fail if the application context cannot start
    }
}
