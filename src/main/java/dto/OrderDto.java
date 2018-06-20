package dto;

import converter.DateConverter;
import converter.MoneyToString;
import entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Getter@Setter
public class OrderDto {

    private Long id;

    private boolean accept;

    private String dateOrder;

    private String dateDeliveredOrder;

    private BigDecimal price;

    private String priceView;

    private String nameCustomer;

    private Collection<FoodDto> foodDtos;

    public OrderDto() {}

    private OrderDto(Order order) {
        this.id = order.getId();
        this.dateOrder = DateConverter.getFormatView().format(order.getDateOrder());
        if (order.getDateDeliveredOrder() != null)
            this.dateDeliveredOrder = DateConverter.getFormatView().format(order.getDateDeliveredOrder());
        this.price = order.getPrice();
        this.accept = order.isAccept();
        this.nameCustomer = order.getUser().getName();
        if (order.getUser().getName() == null)
            this.nameCustomer = order.getUser().getLogin();
        this.priceView = MoneyToString.convert(order.getPrice());
        if (order.getFoods() != null)
            this.foodDtos = FoodDto.convertToDto(order.getFoods());
    }

    public static OrderDto convertToDto(Order order) {
        return order != null ? new OrderDto(order) : null;
    }

    public static List<OrderDto> convertToDto(Collection<Order> orders) {
        List<OrderDto> ordersDto = new ArrayList<>();
        orders.forEach(order -> ordersDto.add(convertToDto(order)));
        return ordersDto;
    }
}
