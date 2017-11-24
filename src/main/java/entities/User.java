package entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class User {

    @Id
    @GeneratedValue()
    private Long id;

    private String username;

    private String password;

    private ArrayList<Expense> expenses;

    private ArrayList<Income> incomes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    public ArrayList<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(ArrayList<Income> incomes) {
        this.incomes = incomes;
    }

    public boolean addExpense(Expense e){
        return expenses.add(e);
    }

    public boolean addIncome(Income e){
        return incomes.add(e);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }




}
