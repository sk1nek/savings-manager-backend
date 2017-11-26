package me.mjaroszewicz.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BalanceChange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String details;

    private Long value;

    private boolean isExpense;

    private Long timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public BalanceChange(){}

    public BalanceChange(String title, String details, Long value, boolean isExpense, Long timestamp) {
        this.title = title;
        this.details = details;
        this.value = value;
        this.isExpense = isExpense;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BalanceChange{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", value=" + value +
                ", isExpense=" + isExpense +
                ", timestamp=" + timestamp +
                '}';
    }
}
