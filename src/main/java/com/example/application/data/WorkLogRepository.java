package com.example.application.data;

import com.example.application.data.models.WorkLog;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    /*
     * The error is here, the WorkLog is mapped to the correct table through the
     * model
     */
    @Query(value = "SELECT w FROM WorkLog w WHERE w.startDate = :date AND w.employee.id = :employeeId")
    List<WorkLog> getMinutesForDay(LocalDate date, long employeeId);
}
