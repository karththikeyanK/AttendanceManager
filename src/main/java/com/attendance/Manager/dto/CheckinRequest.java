package com.attendance.Manager.dto;

import lombok.Data;

@Data
public class CheckinRequest {

    private Integer userId;
    private String checkInTime;
    private String checkinLatitude;
    private String checkinLongitude;

}
