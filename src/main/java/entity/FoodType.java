package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "FoodType")
@Table(name = "food_type")
@Getter @Setter
public class FoodType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "combo")
    private boolean combo;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Food> foods;

    public FoodType(String type) {
        this.type = type;
    }

    public FoodType(String type, boolean isCombo) {
        this.combo = combo;
        this.type = type;
    }

    protected FoodType() {}
}
