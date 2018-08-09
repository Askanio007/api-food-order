package entity;

import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "CompletedOrderItem")
@Table(name = "order_completed_item")
@Getter
@Setter
public class CompletedOrderItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "id_food")
    private Long idFood;

    @Column(name = "name")
    private String name;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "completed_order_id")
    private CompletedOrder completedOrder;

    protected CompletedOrderItem() {}

    public CompletedOrderItem(Food food, CompletedOrder order) {
        this.idFood = food.getId();
        this.name = food.getName();
        this.productCode = food.getProductCode();
        this.price = food.getPrice();
        this.type = food.getType().getType();
        this.completedOrder = order;
    }

    public static List<CompletedOrderItem> createItem(Collection<Food> foods, CompletedOrder order) {
        ArrayList<CompletedOrderItem> items = new ArrayList<>();
        foods.forEach(food -> items.add(new CompletedOrderItem(food, order)));
        return items;
    }

}
