package com.attendance.Manager.consumer;

import com.attendance.Manager.dto.CheckinRequest;
import com.attendance.Manager.dto.CheckoutRequest;
import com.attendance.Manager.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQConsumer {

    @Value("${rabbitmq.queue.checkin.name}")
    private String checkinQueue;

    @Value("${rabbitmq.queue.checkout.name}")
    private String CheckoutQueue;

    @Autowired
    private AttendanceService attendanceService;
    @RabbitListener(queues = "checkin_queue")
    public void checkin(CheckinRequest checkinRequest) {
       log.info("check in received from the RabbitMQ -> " + checkinRequest);
       if(!attendanceService.ischeckin(checkinRequest.getUserId())){
           log.info("data transfer to AttendanceService");
           attendanceService.saveCheckin(checkinRequest);
       }
    }

    @RabbitListener(queues = "checkout_queue")
    public void checkout(CheckoutRequest checkoutRequest) {
        log.info("check out received from the RabbitMQ -> " + checkoutRequest);
        if(attendanceService.ischeckin(checkoutRequest.getUserId())){
            log.info("data transfer to AttendanceService");
            attendanceService.saveCheckout(checkoutRequest);
        }else{
            log.error("User doesn't check in");
        }

    }
}
