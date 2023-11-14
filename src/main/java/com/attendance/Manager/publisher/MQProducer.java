package com.attendance.Manager.publisher;

import com.attendance.Manager.dto.CheckinRequest;
import com.attendance.Manager.dto.CheckoutRequest;
import com.attendance.Manager.exception.GeneralBusinessException;
import com.attendance.Manager.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.checkin.key}")
    private String routingCheckinKey;

    @Value("${rabbitmq.routing.checkout.key}")
    private String routingCheckoutKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AttendanceService attendanceService;




    public void sendCheckinMessage(CheckinRequest checkinRequest){
        log.info(checkinRequest.toString());
        try{
            if (!attendanceService.ischeckin(checkinRequest.getUserId())) {
                if(checkinRequest.getUserId() !=null){
                    rabbitTemplate.convertAndSend(exchange, routingCheckinKey, checkinRequest);
                    log.info("checkin send to RabbitMq "+checkinRequest.toString());
                }else {
                    throw new GeneralBusinessException("User id is null");
                }

            } else {
                throw new GeneralBusinessException("Already check in User with id : " + checkinRequest.getUserId());
            }
        }catch (GeneralBusinessException ex){
            log.error("MQProducer: Already user Checked in");
            throw ex;
        }catch(Exception e){
            log.error("MQProducer: Unexpected error while consuming message from rabbitmq");
            throw new RuntimeException("Unexpected error while consuming message from rabbitmq"+e.getMessage());
        }
    }




    public void sendCheckoutMessage(CheckoutRequest checkoutRequest){
        try{
            if (attendanceService.ischeckin(checkoutRequest.getUserId())) {
                if(checkoutRequest.getUserId() !=null){
                    rabbitTemplate.convertAndSend(exchange, routingCheckoutKey, checkoutRequest);
                    log.info("checkout send to RabbitMq "+checkoutRequest.toString());
                }else {
                    throw new GeneralBusinessException("User id is null");
                }

            }else {
                throw new GeneralBusinessException("User doesn't checked in with id: "+checkoutRequest.getUserId());
            }
            log.info("MQProducer: checkout send to RabbitMq " + checkoutRequest.toString());
        }catch (GeneralBusinessException ex){
            log.error("MQProducer: "+ex.getMessage());
            throw ex;
        }catch (Exception e){
            log.error("MQProducer: Unexpected Error Occurred -->"+e.getMessage());
            throw new RuntimeException("Unexpected Error Occurred"+e.getMessage());
        }
    }


}
