package com.smart_campus.smartAttendance.controller;

import com.smart_campus.smartAttendance.Service.attendanceService;
import com.smart_campus.smartAttendance.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AttendanceController {

    @GetMapping("/index")
    public String home() {
        return "index.html";
    }

    @Autowired
    private attendanceService attendanceService;

    private static final LocalTime ENTRY_START_TIME = LocalTime.of(9, 0);
    private static final LocalTime ENTRY_END_TIME = LocalTime.of(10, 0);
    private static final LocalTime EXIT_START_TIME = LocalTime.of(13, 0);
    private static final LocalTime EXIT_END_TIME = LocalTime.of(14, 0);

    @PostMapping("/attendance/mark/{studentId}")
    public ResponseEntity<String> markAttendance(@PathVariable String studentId) {
        LocalTime currentTime = LocalTime.now();
        LocalDate today = LocalDate.now();

        Attendance existingAttendance = attendanceService.findByStudentIdAndDate(studentId, today);

        if (currentTime.isBefore(ENTRY_START_TIME) || currentTime.isAfter(ENTRY_END_TIME)) {

            if (existingAttendance != null && existingAttendance.getEntryTime() != null) {

                existingAttendance.setExitTime(LocalDateTime.now());
                attendanceService.updateAttendance(existingAttendance);
                return ResponseEntity.ok("Exit marked successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Entry must be marked before exit.");
            }
        } else {

            if (existingAttendance == null) {
                Attendance attendance = new Attendance();
                attendance.setStudentId(studentId);
                attendance.setEntryDate(today);
                attendance.setEntryTime(LocalDateTime.now());
                attendanceService.saveAttendance(attendance);
                return ResponseEntity.ok("Entry marked successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Entry already marked for today.");
            }
        }
    }

  


    private int getTotalClassesInMonth(int month, int year) {
        int totalWeeks = YearMonth.of(year, month).lengthOfMonth() / 7;
        return totalWeeks * 4;
    }

  

    @GetMapping("/attendance/summary/{studentId}/{month}/{year}")
    public ResponseEntity<Map<String, Object>> getAttendanceSummary(@PathVariable String studentId,
            @PathVariable int month,
            @PathVariable int year) {
        List<Attendance> attendanceList = attendanceService.findByStudentIdAndMonth(studentId, month, year);

        long daysAttended = attendanceList.stream()
                .map(Attendance::getEntryDate)
                .distinct()
                .count();

        int totalClasses = getTotalClassesInMonth(month, year);

        Map<String, Object> summary = new HashMap<>();
        summary.put("daysAttended", daysAttended);
        summary.put("totalClasses", totalClasses);

        return ResponseEntity.ok(summary);
    }
}
