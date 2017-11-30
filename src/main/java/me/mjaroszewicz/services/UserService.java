package me.mjaroszewicz.services;

import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.entities.VerificationToken;
import me.mjaroszewicz.exceptions.RegistrationException;
import me.mjaroszewicz.repositories.UserRepository;
import me.mjaroszewicz.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private VerificationTokenRepository tokenRepo;

    @Autowired
    private MailService mailService;

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

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     *
     * @param user Data Transfer Object containing user credentials
     * @return true if length criteria are met (username at least 6 characters / password at least 8)
     */
    private boolean isValidUser(UserDto user){

        if(user.getPassword() == null || user.getUsername() == null)
            return false;

        if((user.getUsername().length() < 6) || (user.getUsername().length() > 32))
            return false;

        return (user.getPassword().length() >= 8) && (user.getPassword().length() <= 32);
    }

    /**
     * Function that changes user password, encodes and forwards it to persistance layer
     *
     * @param usr Target of password change
     * @param pwd New password
     * @return false if password is shorter than 8 characters
     */
    public boolean changeUserPassword(User usr, String pwd) {

        if(pwd.length() < 8)
            return false;

        usr.setPassword(passwordEncoder().encode(pwd));
        userRepo.save(usr);

        return true;
    }

    /**
     * Changes password of currently logged user and passes changes to persistance layer
     *
     * @param pwd New password ( must be at least 8 characters long)
     * @return true if password has met neccessary criteria
     */
    public boolean changeCurrentUserPassword(String pwd){

        if(pwd.length() < 8)
            return false;

        User usr = getLoggedUser();
        usr.setPassword(passwordEncoder().encode(pwd));
        userRepo.save(usr);

        return true;
    }

    /**
     * Creates new verification token and forwards it to persistance layer
     *
     * @param usr Target user
     */
    public VerificationToken createVerificationToken(User usr){

        VerificationToken ret = new VerificationToken();
        ret.setToken(UUID.randomUUID().toString());
        ret.setUser(usr);

        tokenRepo.save(ret);
        return ret;
    }


    public void sendVerificationMail(User usr, String appUrl){

        tokenRepo.delete(tokenRepo.findByUser(usr));

        VerificationToken token = createVerificationToken(usr);
        String recipient = usr.getEmail();
        String confirmationUrl = appUrl + "/registrationConfirm.html?token=" + token.getToken();
        String content = "http://localhost:8080" + confirmationUrl;

        mailService.sendEmail("Registration", content, recipient);
    }



    public VerificationToken getVerificationToken(String token){
        return tokenRepo.findByToken(token);
    }

    public void saveRegisteredUser(User usr){
        userRepo.save(usr);
    }

    /**
     * @return Currently logged user reference
     */
    private User getLoggedUser(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepo.findOneByUsername(auth.getName());
    }

    public boolean changeCurrentUserFirstName(String name){


        if(name.length() < 2 || name.length() > 32)
            return false;

        User usr = getLoggedUser();
        usr.setFirstName(name);
        userRepo.save(usr);

        return true;
    }


}
