package me.mjaroszewicz.dtos;

public class BalanceChangeDto{

    private String title;

    private String details;

    private Long value;

    private boolean isExpense;

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

    @Override
    public String toString() {
        return "BalanceChangeDto{" +
                "title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", value=" + value +
                ", isExpense=" + isExpense +
                '}';
    }
}
