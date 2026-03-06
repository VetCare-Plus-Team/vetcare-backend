package com.vetcare.calculation_service.service;


import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CalculationService {
    public LocalDate calculateNextVaccineDate(String vaccineName, LocalDate administeredDate) {
        if (administeredDate == null) {
            administeredDate = LocalDate.now();
        }
        
      
        switch (vaccineName.toLowerCase()) {
            case "anti-rabies":
            case "parvovirus":
            case "dhppi":
                return administeredDate.plusYears(1); // Year
            case "booster":
            case "deworming":
                return administeredDate.plusDays(21); //in 21 Days
            default:
                return administeredDate.plusMonths(6); // Default 6 Month
        }
    }
}
