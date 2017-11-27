package me.mjaroszewicz.repositories;

import me.mjaroszewicz.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public User findOneByUsername(String username);

    public ArrayList<User> findAll();
}
