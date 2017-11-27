package me.mjaroszewicz.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User implements Serializable{

    @Id
    @GeneratedValue()
    private Long id;

    private String username;

    private String password;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "changes")
    private List<BalanceChange> balanceChanges;


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

    public List<BalanceChange> getBalanceChanges() {
        return balanceChanges;
    }

    public void setBalanceChanges(List<BalanceChange> balanceChanges) {
        this.balanceChanges = balanceChanges;
    }

    public boolean addBalanceChange(BalanceChange e){
        return balanceChanges.add(e);
    }

    public boolean removeBalanceChange(Long id){
        for(BalanceChange bc: balanceChanges){
            if(bc.getId() == id)
                return balanceChanges.remove(bc);
        }

        return false;
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

    @JsonIgnore
    public User getSanitizedUser(){
        User ret = new User();
        ret.username = this.username;
        ret.balanceChanges = this.balanceChanges;
        ret.id = this.id;

        return ret;
    }




}
