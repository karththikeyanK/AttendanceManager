package com.attendance.Manager.controller;

import com.attendance.Manager.dto.AttendanceDto;
import com.attendance.Manager.dto.CheckinRequest;
import com.attendance.Manager.dto.CheckoutRequest;
import com.attendance.Manager.exception.GeneralBusinessException;
import com.attendance.Manager.publisher.MQProducer;
import com.attendance.Manager.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AttendanceController {

    @Autowired
    private MQProducer mqProducer;

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/checkin")
    public ResponseEntity<String> checkin(@RequestBody CheckinRequest checkinRequest) {
        try {
            checkinRequest.setCheckInTime(LocalDateTime.now().toString());
            mqProducer.sendCheckinMessage(checkinRequest);
            return ResponseEntity.ok("Checkin success.....");
        }catch (GeneralBusinessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            checkoutRequest.setCheckOutTime(LocalDateTime.now().toString());
            mqProducer.sendCheckoutMessage(checkoutRequest);
            return ResponseEntity.ok("Checkout success.....");
        }catch (GeneralBusinessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) throws Exception {
        try {
            AttendanceDto attendanceDto = attendanceService.getById(id);
            return ResponseEntity.ok(attendanceDto);
        } catch (GeneralBusinessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        try{
            log.info("AttendanceController::getAll started");
            List<AttendanceDto> attendance = attendanceService.getAll();
            return ResponseEntity.ok(attendance);
        }catch (GeneralBusinessException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
