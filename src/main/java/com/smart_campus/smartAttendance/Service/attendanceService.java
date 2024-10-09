package com.smart_campus.smartAttendance.Service;

import com.smart_campus.smartAttendance.repository.AttendanceRepository;
import com.smart_campus.smartAttendance.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class attendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance findByStudentIdAndDate(String studentId, LocalDate date) {
        return attendanceRepository.findByStudentIdAndEntryDate(studentId, date);
    }

    public void saveAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    public Attendance findByStudentId(String studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public void updateAttendance(Attendance attendance) {
        attendanceRepository.save(attendance); 
    }

    public List<Attendance> findByStudentIdAndMonth(String studentId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1); 
        return attendanceRepository.findByStudentIdAndEntryDateBetween(studentId, startDate, endDate);
    }

}
