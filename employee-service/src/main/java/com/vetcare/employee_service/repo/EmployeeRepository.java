package com.vetcare.employee_service.repo;

import com.vetcare.employee_service.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  List<Employee> findByRole(String role);

  List<Employee> findByEmail(String email);
}
