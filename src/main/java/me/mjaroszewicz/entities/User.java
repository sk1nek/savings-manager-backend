package me.mjaroszewicz.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    @Type(type = "yes_no")
    private boolean enabled;

    @Column(name = "first_name")
    private String firstName;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "changes")
    private List<BalanceChange> balanceChanges;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")    )
    private HashSet<Role> roles;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public HashSet<Role> getRoles() {
        return roles;
    }

    public void setRoles(HashSet<Role> roles) {
        this.roles = roles;
    }

    public boolean addRole(Role role){
        if(roles == null)
            return false;

        return roles.add(role);
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
                ", email='" + email +
                '}';
    }

    @JsonIgnore
    public User getSanitizedUser(){
        User ret = new User();
        ret.username = this.username;
        ret.balanceChanges = this.balanceChanges;
        ret.id = this.id;
        ret.email = this.email;

        return ret;
    }




}
