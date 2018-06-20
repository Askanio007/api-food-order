package scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.MessageService;

@Component
public class NotifyAboutOrder implements Runnable {

    private static final Logger log = Logger.getLogger(NotifyAboutOrder.class);

    @Autowired
    private MessageService messageService;

    @Override
    public void run() {
        try {
            messageService.createNotificationAboutOrder();
        } catch (Exception e) {
            log.error("Sending notify is failed", e);
        }
    }
}
