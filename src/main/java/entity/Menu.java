package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "menu")
@Getter @Setter
public class Menu {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "active")
    private boolean active;

    @Column(name = "date_reg_menu")
    private Date dateRegistrationMenu;

    @Column(name = "date_accept_order")
    private Date dateAcceptingOrder;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "menu_foods",
            joinColumns = { @JoinColumn(name = "menu_id") },
            inverseJoinColumns = { @JoinColumn(name = "food_id") }
    )
    private Collection<Food> foods;

    protected Menu() {}

    public Menu(Date dateRegistrationMenu, Date dateAcceptingOrder, Collection<Food> foods) {
        this.dateAcceptingOrder = dateAcceptingOrder;
        this.dateRegistrationMenu = dateRegistrationMenu;
        this.foods = foods;
        this.active = true;
    }




}
