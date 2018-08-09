package scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service.SettingService;
import service.UserService;
import utils.DateBuilder;

@Component
public class UpdateUserBalance {

    private static final Logger log = Logger.getLogger(UpdateUserBalance.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SettingService settingService;

    @Scheduled(cron = "0 0 1 * * *")
    public void updateUserBalance() {
        int updateDay = settingService.getStartAccountingPeriod();
        int todayDay = DateBuilder.todayDay();
        log.info("Is it day update user balance?");
        if (todayDay == updateDay) {
            log.info("Yes! It's today!");
            userService.updateUserBalance();
        } else {
            log.info("No. Not today.");
        }
    }
}
