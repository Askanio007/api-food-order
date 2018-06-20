package dto;

import entity.ProviderOrders;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderOrderDto {

    private String code;
    private String idOrder;

    public ProviderOrderDto() {}
    private ProviderOrderDto(ProviderOrders providerOrders) {
        this.code = providerOrders.getCodeOrder();
        this.idOrder = providerOrders.getIdOrder();
    }

    public static ProviderOrderDto convertToDto(ProviderOrders providerOrders) {
        return new ProviderOrderDto(providerOrders);
    }
}
