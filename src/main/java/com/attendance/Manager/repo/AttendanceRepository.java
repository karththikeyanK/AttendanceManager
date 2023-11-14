package com.attendance.Manager.repo;

import com.attendance.Manager.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository  extends JpaRepository<Attendance, Integer> {


    Attendance findByUserId(int userId);
}
