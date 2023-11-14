package com.attendance.Manager.dto;

import lombok.Data;

@Data
public class CheckoutRequest {
    private Integer userId;
    private String checkOutTime;
    private String checkoutLatitude;
    private String checkoutLongitude;

}
