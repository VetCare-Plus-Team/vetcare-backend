package com.vetcare.log_service.repo;

import com.vetcare.log_service.entity.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LogRepository extends JpaRepository<LogEvent, Long> {

}
