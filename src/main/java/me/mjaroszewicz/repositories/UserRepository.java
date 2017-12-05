package me.mjaroszewicz.repositories;

import me.mjaroszewicz.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findOneByUsername(String username);

    ArrayList<User> findAll();

    ArrayList<User> findFirst100();

    User findByEmail(String email);


}
