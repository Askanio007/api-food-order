package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "order_completed")
@Getter
@Setter
public class CompletedOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "id_order")
    private Long idOrder;

    @Column(name = "date")
    private Date dateOrder;

    @Column(name = "date_delivered_order")
    private Date dateDeliveredOrder;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "user_id")
    private Long idUser;

    @OneToMany(mappedBy = "completedOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<CompletedOrderItem> items;

    public CompletedOrder(Order order) {
        this.idOrder = order.getId();
        this.dateDeliveredOrder = order.getDateDeliveredOrder();
        this.dateOrder = order.getDateOrder();
        this.price = order.getPrice();
        this.idUser = order.getUser().getId();
    }

    protected CompletedOrder() {
    }
}
