package models.pushNotification;

import converter.DateConverter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Message {

    private String title;
    private String body;
    private String icon;
    private String click_action;

    private Message(){
        this.icon = "https://cdn.lifehacker.ru/wp-content/uploads/2015/07/Depositphotos_9295917_original_1436784394.jpg";
        this.click_action = "https://foodorder.tst.local/profile";
    }

    public static Message menuWasAdded(Date dateAcceptOrder) {
        Message message = new Message();
        message.setTitle("Меню разместили!");
        message.setBody("Заказ будет принят в " + DateConverter.getFormatTime().format(dateAcceptOrder));
        return message;
    }

    public static Message tenMinutesLeft() {
        Message message = new Message();
        message.setIcon("https://cdn.playbuzz.com/cdn/288c21e7-bf2e-4aa1-9ce6-644b9f42aeee/2ffea2ef-64ca-4e72-a0ea-2203cce89941_560_420.jpg");
        message.setTitle("Ты поторопись...");
        message.body = "Время заказа закончится через 10 минут!";
        return message;
    }
}