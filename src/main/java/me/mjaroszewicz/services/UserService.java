package me.mjaroszewicz.services;

import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.entities.VerificationToken;
import me.mjaroszewicz.exceptions.RegistrationException;
import me.mjaroszewicz.repositories.UserRepository;
import me.mjaroszewicz.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private VerificationTokenRepository tokenRepo;

    @Transactional
    public User registerNewUserAccount(UserDto userdto) throws RegistrationException {

        if(!isValidUser(userdto)) throw new RegistrationException("DTO didn't meet specified criteria");

        if(userRepo.findByEmail(userdto.getEmail()) != null)
            throw new RegistrationException("Username already taken");

        User user = new User();
        user.setUsername(userdto.getUsername());
        user.setPassword(passwordEncoder().encode(userdto.getPassword()));
        user.setEmail(userdto.getEmail());

        return userRepo.save(user);

    }

//    @PostConstruct
//    private void init() throws RegistrationException{
//        registerNewUserAccount(new UserDto("username", "password"));
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private boolean isValidUser(UserDto user){

        if(user.getPassword() == null || user.getUsername() == null)
            return false;

        if((user.getUsername().length() < 6) || (user.getUsername().length() > 32))
            return false;

        if((user.getPassword().length() < 8) || (user.getPassword().length() > 32))
            return false;

        return true;
    }

    public VerificationToken createVerificationToken(User usr, String token){
        VerificationToken ret = new VerificationToken();
        ret.setToken(token);
        ret.setUser(usr);

        tokenRepo.save(ret);
        return ret;
    }

    public VerificationToken getVerificationToken(String token){
        return tokenRepo.findByToken(token);
    }

    public void saveRegisteredUser(User usr){
        userRepo.save(usr);
    }


}
