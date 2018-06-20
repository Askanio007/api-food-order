package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity(name = "AutoOrder")
@Table(name = "auto_order")
public class AutoOrder {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private User user;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "active")
    private boolean active;

    @Column(name = "type")
    private String type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "auto_order_foods",
            joinColumns = { @JoinColumn(name = "auto_order_id") },
            inverseJoinColumns = { @JoinColumn(name = "food_id") }
    )
    private Collection<Food> foods;

    public AutoOrder() {
        this.active = false;
    }

    public void clear() {
        this.active = false;
        this.type = null;
    }
}
