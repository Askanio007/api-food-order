package scheduler.pushNotification;

import dto.MenuDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.MenuService;
import service.MessageService;

@Component
public class MenuWasCreate implements Runnable {

    private static final Logger log = Logger.getLogger(MenuWasCreate.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private MenuService menuService;

    @Override
    public void run() {
        try {
            MenuDto menuDto = menuService.getTodayMenu();
            messageService.menuWasCreate(menuDto.getDateAcceptOrderD());
        } catch (Exception e) {
            log.error("Sending notify about create menu is failed", e);
        }
    }
}
