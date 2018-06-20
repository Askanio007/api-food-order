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

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private enums.FoodType type;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Food> foods;

    public FoodType(enums.FoodType type) {
        this.type = type;
    }

    protected FoodType() {}
}
