package dto;

import converter.DateConverter;
import converter.MoneyToString;
import entity.Transaction;
import enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TransactionDto {

    private long id;

    @NotNull
    @DecimalMin(value = "1")
    private BigDecimal sum;

    private String sumView;

    private String status;

    private String statusView;

    private String createDate;

    private String updateDate;

    @NotNull
    private String comment;

    private TransactionDto(Transaction tr) {
        this.id = tr.getId();
        this.status = tr.getStatus().getStatus().name();
        this.sum = tr.getTransactionAmount();
        this.createDate = DateConverter.getFormatView().format(tr.getCreateDate());
        this.updateDate = DateConverter.getFormatView().format(tr.getUpdateDate());
        this.statusView = TransactionStatus.getStatus(tr.getStatus().getStatus());
        this.sumView = MoneyToString.convert(tr.getTransactionAmount());
        this.comment = tr.getComment();
    }

    public static TransactionDto convertToDto(Transaction tr) {
        return tr != null ? new TransactionDto(tr) : null;
    }

    public static List<TransactionDto> convertToDto(List<Transaction> trs) {
        List<TransactionDto> trDtos = new ArrayList<>();
        trs.forEach(transaction -> trDtos.add(convertToDto(transaction)));
        return trDtos;
    }

    public TransactionDto(BigDecimal sum) {
        this.sum = sum;
    }

    public TransactionDto(BigDecimal sum, String comment) {
        this.comment = comment;
        this.sum = sum;
    }

    private TransactionDto() {}
}
