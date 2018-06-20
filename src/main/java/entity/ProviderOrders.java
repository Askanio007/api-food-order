package entity;

import enums.RequestState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "provider_orders")
@Setter
@Getter
public class ProviderOrders {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "date_send")
    private Date dateSend;

    @Column(name = "request_state")
    @Enumerated(EnumType.STRING)
    private RequestState requestState;

    @Column(name = "response")
    @Type(type="text")
    private String response;

    @Column(name = "request")
    @Type(type="text")
    private String request;

    @Column(name = "id_order")
    @Type(type="text")
    private String idOrder;

    @Column(name = "code_order")
    @Type(type="text")
    private String codeOrder;

    public ProviderOrders() {}
}
