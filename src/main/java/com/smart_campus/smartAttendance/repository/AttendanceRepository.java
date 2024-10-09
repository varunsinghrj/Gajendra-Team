package com.smart_campus.smartAttendance.repository;

import com.smart_campus.smartAttendance.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    Attendance findByStudentIdAndEntryDate(String studentId, LocalDate entryDate);

    Attendance findByStudentId(String studentId);

    List<Attendance> findByStudentIdAndEntryDateBetween(String studentId, LocalDate startDate, LocalDate endDate);
}
