package at.scintillation.talks.meimarie.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:alexander.rosemann@scintillation.at">Alexander Rosemann</a>
 * @since 1.0.0
 */
public class Transaction {

    private UUID id;
    private Date date;
    private Double amount;
    private TransactionType transactionType;
    private List<String> tags;

    public Transaction() {
    }

    public Transaction(UUID id, Date date, Double amount, TransactionType transactionType, List<String> tags) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.transactionType = transactionType;
        this.tags = tags;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", amount=" + amount +
                ", transactionType=" + transactionType +
                ", tags=" + tags +
                '}';
    }
}
