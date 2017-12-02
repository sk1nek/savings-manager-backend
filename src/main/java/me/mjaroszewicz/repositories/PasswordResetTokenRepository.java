package me.mjaroszewicz.repositories;

import me.mjaroszewicz.entities.PasswordResetToken;
import me.mjaroszewicz.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    PasswordResetToken findOneByUser(User user);

    PasswordResetToken findOneByToken(String token);

}
