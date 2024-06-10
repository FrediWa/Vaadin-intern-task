package com.example.application.data;

import com.example.application.data.models.Employee;
import com.example.application.data.models.WorkLog;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(value = "UPDATE WorkLog w SET w.startTime = :time WHERE w.startDate = :date AND w.employee = :employee")
    void syncArrivalTimes(LocalDate date, LocalTime time, Employee employee);

    @Modifying
    @Query(value = "UPDATE WorkLog w SET w.absent = :absentTime WHERE w.startDate = :date AND w.employee = :employee")
    void syncAbsentTimes(LocalDate date, int absentTime, Employee employee);

}
