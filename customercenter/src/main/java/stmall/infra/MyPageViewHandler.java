package stmall.infra;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import stmall.config.kafka.KafkaProcessor;
import stmall.domain.*;

@Service
public class MyPageViewHandler {

    @Autowired
    private MyPageRepository myPageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderPlaced_then_CREATE_1(@Payload OrderPlaced orderPlaced) {
        try {
            if (!orderPlaced.validate()) return;

            MyPage myPage = new MyPage();
            myPage.setOrderId(orderPlaced.getId());
            myPage.setUserId(orderPlaced.getUserId());
            myPage.setProductName(orderPlaced.getProductName());
            myPage.setProductId(orderPlaced.getProductId());
            myPage.setQty(orderPlaced.getQty());
            myPageRepository.save(myPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_1(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (!deliveryStarted.validate()) return;

            List<MyPage> myPageList = myPageRepository.findByOrderId(
                deliveryStarted.getOrderId()
            );
            for (MyPage myPage : myPageList) {
                myPage.setStatus(deliveryStarted.getStatus());
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCancelled_then_UPDATE_2(@Payload DeliveryCancelled deliveryCancelled) {
        try {
            if (!deliveryCancelled.validate()) return;

            List<MyPage> myPageList = myPageRepository.findByOrderId(
                deliveryCancelled.getOrderId()
            );
            for (MyPage myPage : myPageList) {
                myPage.setStatus(deliveryCancelled.getStatus());
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
