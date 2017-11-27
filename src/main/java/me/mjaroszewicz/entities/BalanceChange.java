package me.mjaroszewicz.entities;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class BalanceChange implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "details")
    private String details;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "is_expense")
    @Type(type = "yes_no")
    private boolean isExpense;

    @Column(name = "timestamp")
    @Type(type = "long")
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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
        this.amount = value;
        this.isExpense = isExpense;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BalanceChange{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", amount=" + amount +
                ", isExpense=" + isExpense +
                ", timestamp=" + timestamp +
                '}';
    }
}
