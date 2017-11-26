package me.mjaroszewicz.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class User implements Serializable{

    @Id
    @GeneratedValue()
    private Long id;

    private String username;

    private String password;

    private ArrayList<BalanceChange> balanceChanges;

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

    public ArrayList<BalanceChange> getBalanceChanges() {
        return balanceChanges;
    }

    public void setBalanceChanges(ArrayList<BalanceChange> balanceChanges) {
        this.balanceChanges = balanceChanges;
    }

    public boolean addBalanceChange(BalanceChange e){
        return balanceChanges.add(e);
    }

    public User(){
        this.balanceChanges = new ArrayList<>();
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
