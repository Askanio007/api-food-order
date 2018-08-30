package scheduler.pushNotification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.MessageService;

@Component
public class OrderWasDelivered implements Runnable {

    private static final Logger log = Logger.getLogger(OrderWasDelivered.class);

    @Autowired
    private MessageService messageService;

    @Override
    public void run() {
        try {
            messageService.orderWasDelivered();
        } catch (Exception e) {
            log.error("Sending notify about send order to provider is failed", e);
        }
    }
}
