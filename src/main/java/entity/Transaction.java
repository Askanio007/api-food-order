package entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private TransactionStatus status;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "comment")
    @Type(type="text")
    private String comment;

    public static Transaction createTransaction(BigDecimal sum, TransactionStatus status, String comment) {
        Transaction tr = new Transaction();
        Date today = new Date();
        tr.setCreateDate(today);
        tr.setUpdateDate(today);
        tr.setStatus(status);
        tr.setTransactionAmount(sum);
        tr.setComment(comment);
        return tr;
    }
}
