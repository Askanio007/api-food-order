package models.pushNotification;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Notification {

    private Message notification;
    private List<String> registration_ids;

    public Notification(Date dateAcceptOrder) {
        this.notification = Message.menuWasAdded(dateAcceptOrder);
        this.registration_ids = new ArrayList<>();
    }

    public Notification() {
        this.notification = Message.tenMinutesLeft();
        this.registration_ids = new ArrayList<>();
    }

    public void addDevice(String device) {
        this.registration_ids.add(device);
    }

    public void addDevice(List<String> device) {
        this.registration_ids.addAll(device);
    }
}
