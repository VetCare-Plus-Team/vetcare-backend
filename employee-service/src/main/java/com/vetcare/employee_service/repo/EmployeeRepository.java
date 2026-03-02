package com.vetcare.employee_service.repo;

import com.vetcare.employee_service.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // get employee details by user id
    Optional<Employee> findByUserId(Long userId);
}
