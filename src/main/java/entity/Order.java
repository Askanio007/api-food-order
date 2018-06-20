package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "order_user")
@Getter
@Setter
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "accept")
    private boolean accept;

    @Column(name = "date")
    private Date dateOrder;

    @Column(name = "date_delivered_order")
    private Date dateDeliveredOrder;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_foods",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "food_id")}
    )
    private Collection<Food> foods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Order() {
    }

    public Order(User user, Collection<Food> foods, BigDecimal price) {
        this.accept = false;
        this.price = price;
        this.dateOrder = new Date();
        this.user = user;
        this.foods = foods;
    }

}
