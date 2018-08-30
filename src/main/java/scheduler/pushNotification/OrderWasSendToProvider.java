package scheduler.pushNotification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.MessageService;

@Component
public class OrderWasSendToProvider implements Runnable {

    private static final Logger log = Logger.getLogger(OrderWasSendToProvider.class);

    @Autowired
    private MessageService messageService;

    @Override
    public void run() {
        try {
            messageService.orderSendToProvider();
        } catch (Exception e) {
            log.error("Sending notify about create order is failed", e);
        }
    }

}
