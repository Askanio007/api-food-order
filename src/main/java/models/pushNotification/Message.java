package models.pushNotification;

import converter.DateConverter;
import dto.ProviderOrderDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
        this.click_action = "https://k.solidsystems.ru/profile";
    }

    public static Message menuWasAdded(Date dateAcceptOrder) {
        Message message = new Message();
        message.setTitle("Меню разместили!");
        message.setBody("Заказ будет принят в " + DateConverter.getFormatTime().format(dateAcceptOrder));
        return message;
    }

    public static Message tenMinutesLeft() {
        Message message = new Message();
        message.setTitle("Ты поторопись...");
        message.setBody("Время заказа закончится через 10 минут!");
        return message;
    }

    public static Message orderWasCreate(ProviderOrderDto providerOrderDto, BigDecimal allSumTodayOrder) {
        Message message = new Message();
        message.setTitle("Solid Systems отправили заказ");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer
                .append("ID заказа - ")
                .append(providerOrderDto.getIdOrder())
                .append(", Код - ")
                .append(providerOrderDto.getCode())
                .append(", Сумма - ")
                .append(allSumTodayOrder.setScale(0).toString())
                .append(" руб.");
        message.setBody(stringBuffer.toString());
        message.setClick_action("");
        return message;
    }

    public static Message orderWasDelivered() {
        Message message = new Message();
        message.setTitle("Можно идти обедать!");
        message.setBody("Сегодняшний заказ доставлен. Приятного аппетита!");
        return message;
    }
}