package enums;

public enum TransactionStatus {
    SEND_TO_MM(1), COMPLETE(2), PAYMENT_ORDER(3);

    private long id;

    public static String getStatus(TransactionStatus status) {
        switch(status) {
            case SEND_TO_MM:  return "Отправлен";
            case COMPLETE:  return "Подтверждён";
            case PAYMENT_ORDER:  return "Оплата заказа";
        }
        return null;
    }

    TransactionStatus(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
