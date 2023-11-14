package com.attendance.Manager.service;

import com.attendance.Manager.dto.AttendanceDto;
import com.attendance.Manager.dto.CheckinRequest;
import com.attendance.Manager.dto.CheckoutRequest;
import com.attendance.Manager.entity.Attendance;
import com.attendance.Manager.exception.GeneralBusinessException;
import com.attendance.Manager.repo.AttendanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public CheckinRequest saveCheckin(CheckinRequest checkinRequest){
        log.info("AttendanceService saveCheckin start");
        try{
            if (attendanceRepository.existsById(checkinRequest.getUserId())){
                throw new GeneralBusinessException("User already checked in id : "+checkinRequest.getUserId()+" at time : "+checkinRequest.getCheckInTime());
            }
            Attendance attendance = new Attendance();
            attendance.setUserId(checkinRequest.getUserId());
            attendance.setCheckInTime(checkinRequest.getCheckInTime());
            attendance.setCheckinLatitude(checkinRequest.getCheckinLatitude());
            attendance.setCheckinLongitude(checkinRequest.getCheckinLongitude());
            log.info("AttendanceService saveCheckin end");
            attendanceRepository.save(attendance);
            return checkinRequest;
        }catch (GeneralBusinessException ex){
            log.error("AttendanceService saveCheckin error : "+ex.getMessage());
            throw ex;
        } catch (Exception e){
            log.error("AttendanceService saveCheckin unexpected error : "+e.getMessage());
            throw new RuntimeException("Unexpected error occurred"+e.getMessage());
        }

    }

    public void saveCheckout(CheckoutRequest checkoutRequest) {
        try{
            log.info("Attendance Service: Checkout started..");
            Attendance attendance = attendanceRepository.findByUserId(checkoutRequest.getUserId());
            if (attendance != null) {
                attendance.setCheckOutTime(checkoutRequest.getCheckOutTime());
                attendance.setCheckOutLatitude(checkoutRequest.getCheckoutLatitude());
                attendance.setCheckOutLongitude(checkoutRequest.getCheckoutLongitude());
                attendanceRepository.save(attendance);
            }else {
                throw new GeneralBusinessException("User not found id : "+checkoutRequest.getUserId());
            }
        }catch (GeneralBusinessException ex){
            log.error("Attendance Service: Error Occured"+ ex.getMessage());
            throw ex;
        }catch (Exception e){
            log.error("Attendance Service Error Occred :"+e.getMessage());
            throw new RuntimeException("Unexpected error occurred"+e.getMessage());
        }
    }

    public boolean ischeckin(int userId) {
        Attendance attendance = attendanceRepository.findByUserId(userId);
        return attendance != null;
    }

    public AttendanceDto getById(Integer id) {
        try{

            if(ischeckin(id)){
                Attendance attendance = attendanceRepository.findByUserId(id);
                AttendanceDto attendanceDto = new AttendanceDto();
                attendanceDto.setId(attendance.getId());
                attendanceDto.setUserId(attendance.getUserId());
                attendanceDto.setCheckInTime(attendance.getCheckInTime());
                attendanceDto.setCheckOutTime(attendance.getCheckOutTime());
                attendanceDto.setCheckInLatitude(attendance.getCheckinLatitude());
                attendanceDto.setCheckInLongitude(attendance.getCheckinLongitude());
                attendanceDto.setCheckOutLatitude(attendance.getCheckOutLatitude());
                attendanceDto.setCheckOutLongitude(attendance.getCheckOutLongitude());
                return attendanceDto;
            }else{
                log.error("AttendanceService::User not found id : "+id);
                throw new GeneralBusinessException("User not found id : "+id);
            }
        }catch (GeneralBusinessException ex){
            log.error("AttendanceService::Error getById: " + ex.getMessage());
            throw ex;
        }
        catch (Exception ex){
            log.error("AttendanceService::Error getById: " + ex.getMessage());
            throw new RuntimeException("Error in AttendanceService::getById"+ex.getMessage());
        }
    }

    public List<AttendanceDto> getAll() {
        try{
            List<Attendance> attendances = attendanceRepository.findAll();
            AttendanceDto attendanceDto = new AttendanceDto();
            if (attendances.isEmpty()){
                throw new GeneralBusinessException("There is no attendance");
            }else {
                List<AttendanceDto> attendanceDtos = new java.util.ArrayList<>();
                for (Attendance attendance : attendances){
                    attendanceDto.setId(attendance.getId());
                    attendanceDto.setUserId(attendance.getUserId());
                    attendanceDto.setCheckInTime(attendance.getCheckInTime());
                    attendanceDto.setCheckOutTime(attendance.getCheckOutTime());
                    attendanceDto.setCheckInLatitude(attendance.getCheckinLatitude());
                    attendanceDto.setCheckInLongitude(attendance.getCheckinLongitude());
                    attendanceDto.setCheckOutLatitude(attendance.getCheckOutLatitude());
                    attendanceDto.setCheckOutLongitude(attendance.getCheckOutLongitude());
                    attendanceDtos.add(attendanceDto);
                }
                return attendanceDtos;
            }
        }catch (GeneralBusinessException ex){
            log.error("AttendanceService::Error getAll: " + ex.getMessage());
            throw ex;
        } catch (Exception ex){
            log.error("AttendanceService::Error getAll: " + ex.getMessage());
            throw new RuntimeException("Unexpected error occurred while getting data from database"+ex.getMessage());
        }
    }
}
