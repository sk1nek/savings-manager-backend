package me.mjaroszewicz.repositories;

import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.entities.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User usr);
}
