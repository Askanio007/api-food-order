package scheduler.pushNotification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.MessageService;

@Component
public class SmallTimeForOrder implements Runnable {

    private static final Logger log = Logger.getLogger(SmallTimeForOrder.class);

    @Autowired
    private MessageService messageService;

    @Override
    public void run() {
        try {
            messageService.smallTimeForOrder();
        } catch (Exception e) {
            log.error("Sending notify about time order is failed", e);
        }
    }
}
