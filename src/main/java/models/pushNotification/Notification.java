package models.pushNotification;

import dto.ProviderOrderDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Notification {

    private Message notification;
    private List<String> registration_ids;

    private Notification() {
    }

    private Notification(Message message, List<String> deviceIds) {
        this.notification = message;
        this.registration_ids = deviceIds;
    }

    public void addDevice(String device) {
        this.registration_ids.add(device);
    }

    public void addDevice(List<String> device) {
        this.registration_ids.addAll(device);
    }

    public static Notification aboutMenu(Date dateAcceptOrder, List<String> devices) {
        return new Notification(Message.menuWasAdded(dateAcceptOrder), devices);
    }

    public static Notification aboutTimeOrder(List<String> devices) {
        return new Notification(Message.tenMinutesLeft(), devices);
    }

    public static Notification orderWasCreate(ProviderOrderDto providerOrderDto, BigDecimal allSumTodayOrder, List<String> devices) {
        return new Notification(Message.orderWasCreate(providerOrderDto, allSumTodayOrder), devices);
    }

    public static Notification orderWasDelivered(List<String> devices) {
        return new Notification(Message.orderWasDelivered(), devices);
    }
}
