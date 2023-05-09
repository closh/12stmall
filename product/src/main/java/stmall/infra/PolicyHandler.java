package stmall.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import stmall.config.kafka.KafkaProcessor;
import stmall.domain.*;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    InventoryRepository inventoryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(value = KafkaProcessor.INPUT,condition = "headers['type']=='DeliveryStarted'")
    public void wheneverDeliveryStarted_StockDecrease(@Payload DeliveryStarted deliveryStarted) {
        DeliveryStarted event = deliveryStarted;
        Inventory.stockDecrease(event);
    }

    @StreamListener(value = KafkaProcessor.INPUT,condition = "headers['type']=='DeliveryCancelled'")
    public void wheneverDeliveryCancelled_StockIncrease(@Payload DeliveryCancelled deliveryCancelled) {
        DeliveryCancelled event = deliveryCancelled;
        Inventory.stockIncrease(event);
    }
}
