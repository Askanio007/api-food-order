package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Entity(name = "Food")
@Table(name = "food")
@Getter
@Setter
public class Food {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "active")
    private boolean active;

    @Column(name = "available_every_day")
    private boolean availableEveryDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private FoodType type;

    @Column(name = "name")
    private String name;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToMany(mappedBy = "foods")
    private Collection<Order> orders;

    @ManyToMany(mappedBy = "foods")
    private Collection<Menu> menu;

    @ManyToMany(mappedBy = "foods")
    private Collection<AutoOrder> autoOrders;

    protected Food() {
    }

    public Food(String name, BigDecimal price, FoodType foodType, boolean available, String productCode) {
        this.name = name.trim();
        this.price = price;
        this.type = foodType;
        this.availableEveryDay = available;
        this.active = true;
        this.productCode = productCode;
    }
}