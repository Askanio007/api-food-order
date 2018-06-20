package service;

import entity.Device;
import models.pushNotification.Notification;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    private static final Logger log = Logger.getLogger(MessageService.class);

    private final String KEY = "key=AAAA9sXPNqw:APA91bGz5bloUsj2W8DI0DKtFEmVHYioL827NrRE1sXnwPJMWx-b8A7p11GKzMkTgKk6uqba7eC5SdvZ1KTstaMhnKIZiirTEwGmO9Z4EwLoe1VbyJhTb_9vz3OwXpyRol7eSEyGoYn9";
    private final String URL_FIREBASE = "https://fcm.googleapis.com/fcm/send";

    @Autowired
    private DeviceService deviceService;

    private void sendNotification(Notification notification) {
        if (notification == null) {
            log.info("Notification is null");
            return;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", KEY);
        HttpEntity<Notification> request = new HttpEntity<>(notification, headers);
        log.info("Send notification in firebase...");
        ResponseEntity<String> res = restTemplate.postForEntity(URL_FIREBASE, request, String.class);
        log.info(res.getStatusCode());
        log.info(res.getBody());
    }

    public void createNotificationAboutMenu(Date date) {
        Notification notification = new Notification(date);
        notification.addDevice(getDevice());
        sendNotification(notification);
    }

    public void createNotificationAboutOrder() {
        Notification notification = new Notification();
        notification.addDevice(getDevice());
        sendNotification(notification);
    }

    private List<String> getDevice() {
        List<Device> devices = deviceService.allDevices();
        List<String> listDevice = new ArrayList<>();
        devices.forEach(device -> listDevice.add(device.getDeviceId()));
        return listDevice;
    }

}
