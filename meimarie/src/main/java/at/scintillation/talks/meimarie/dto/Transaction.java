package at.scintillation.talks.meimarie.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:alexander.rosemann@scintillation.at">Alexander Rosemann</a>
 * @since 1.0.0
 */
@Document(indexName = "meimarie", type = "transaction")
public class Transaction {

    @Id
    private String id;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private Date date;

    @Field(type = FieldType.Double)
    private Double amount;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private TransactionType transactionType;

    @Field(type = FieldType.String, searchAnalyzer = "standard", indexAnalyzer = "standard")
    private List<String> tags;

    public Transaction() {
    }

    public Transaction(String id, Date date, Double amount, TransactionType transactionType, List<String> tags) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.transactionType = transactionType;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
