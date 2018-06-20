package enums;

public enum StatusOrder {
    MENU_NOT_EXIST, MENU_IS_ACTIVE, WAITING_DELIVERY, ORDER_DELIVERED;

    public static String getType(StatusOrder statusOrder) {
        switch(statusOrder) {
            case MENU_NOT_EXIST:  return "Меню пока не разместили";
            case MENU_IS_ACTIVE:  return "Меню разместили, можно заказывать";
            case WAITING_DELIVERY:  return "Заказ отправлен. Ожидается доставка";
            case ORDER_DELIVERED:  return "Заказ доставлен";
        }
        return "";
    }
}
