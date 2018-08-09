package dto;

import converter.DateConverter;
import converter.MoneyToString;
import entity.CompletedOrder;
import entity.CompletedOrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CompletedOrderDto {

    private Long id;

    private String dateOrder;

    private String dateDeliveredOrder;

    private BigDecimal price;

    private String priceView;

    private String nameCustomer;

    private Collection<CompletedOrderItemDto> foodDtos;

    public CompletedOrderDto() {}

    private CompletedOrderDto(CompletedOrder order) {
        this.id = order.getId();
        this.dateOrder = DateConverter.getFormatView().format(order.getDateOrder());
        if (order.getDateDeliveredOrder() != null)
            this.dateDeliveredOrder = DateConverter.getFormatView().format(order.getDateDeliveredOrder());
        this.price = order.getPrice();
        this.nameCustomer = order.getUser().getLogin();
        if (order.getUser().getName() != null)
            this.nameCustomer = order.getUser().getName();
        this.priceView = MoneyToString.convert(order.getPrice());
        if (order.getItems() != null)
            this.foodDtos = CompletedOrderItemDto.convertToDto(order.getItems());
    }

    public static CompletedOrderDto convertToDto(CompletedOrder order) {
        return order != null ? new CompletedOrderDto(order) : null;
    }

    public static List<CompletedOrderDto> convertToDto(Collection<CompletedOrder> orders) {
        List<CompletedOrderDto> ordersDto = new ArrayList<>();
        orders.forEach(order -> ordersDto.add(convertToDto(order)));
        return ordersDto;
    }

}
