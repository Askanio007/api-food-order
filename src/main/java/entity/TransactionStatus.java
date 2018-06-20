package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "transaction_status")
@Getter
@Setter
public class TransactionStatus {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private enums.TransactionStatus status;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Transaction> transactions;


    protected TransactionStatus() { }
}
