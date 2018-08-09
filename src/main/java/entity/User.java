package entity;

import dto.UserDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import utils.EncryptingString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Table(name = "user_fo")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Order> orders;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Device> devices;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<CompletedOrder> completedOrders;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AutoOrder autoOrder;

    protected User() {
    }

    public User(UserDto userDto, Role role) {
        this.login = userDto.getLogin();
        this.password = EncryptingString.getEncoder().encode(userDto.getPassword());
        this.role = role;
        this.enabled = userDto.isEnable();
        this.balance = userDto.getBalance();
        this.autoOrder = new AutoOrder();
    }
}
