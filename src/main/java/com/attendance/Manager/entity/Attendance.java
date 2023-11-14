package com.attendance.Manager.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "Attendance"
)
public class Attendance {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "user_id" ,nullable = false)
    private int userId;

    @Column(name = "check_in_time")
    private String checkInTime;

    @Column(name = "check_out_time")
    private String checkOutTime;

    @Column(name="checkInLatitude")
    private String checkinLatitude;

    @Column(name="checkinLongitude")
    private String checkinLongitude;

    @Column(name="checkOutLatitude")
    private String checkOutLatitude;

    @Column(name="checkOutLongitude")
    private String checkOutLongitude;

}
