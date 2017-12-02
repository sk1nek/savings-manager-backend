package me.mjaroszewicz.services;

import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.PasswordResetToken;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.entities.VerificationToken;
import me.mjaroszewicz.exceptions.RegistrationException;
import me.mjaroszewicz.repositories.PasswordResetTokenRepository;
import me.mjaroszewicz.repositories.UserRepository;
import me.mjaroszewicz.repositories.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private VerificationTokenRepository verificationTokenRepo;

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepo;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Transactional
    public User registerNewUserAccount(UserDto userdto) throws RegistrationException {

        if(!isValidUser(userdto)) throw new RegistrationException("DTO didn't meet specified criteria");

        if(userRepo.findByEmail(userdto.getEmail()) != null)
            throw new RegistrationException("Username already taken");

        User user = new User();
        user.setUsername(userdto.getUsername());
        user.setPassword(passwordEncoder().encode(userdto.getPassword()));
        user.setEmail(userdto.getEmail());
        user.setFirstName(userdto.getFirstName());

        return userRepo.save(user);
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

        verificationTokenRepo.save(ret);
        return ret;
    }


    public void sendVerificationMail(User usr, String appUrl){

        verificationTokenRepo.delete(verificationTokenRepo.findByUser(usr));

        VerificationToken token = createVerificationToken(usr);
        String recipient = usr.getEmail();
        String confirmationUrl = appUrl + "/registrationConfirm.html?token=" + token.getToken();
        String content = "http://localhost:8080" + confirmationUrl;

        mailService.sendEmail("Registration", content, recipient);
    }


    public VerificationToken getVerificationToken(String token){
        return verificationTokenRepo.findByToken(token);
    }

    /*
    Password reset methods below
     */

    public PasswordResetToken createPasswordResetToken(User usr){
        PasswordResetToken ret = new PasswordResetToken();
        ret.setToken(UUID.randomUUID().toString());
        ret.setUser(usr);

        passwordResetTokenRepo.save(ret);
        return ret;
    }

    public PasswordResetToken getPasswordResetToken(String token){
        return passwordResetTokenRepo.findOneByToken(token);
    }

    /**/

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

    public boolean changeUserFirstName(User usr, String name){

        log.error(usr.toString());

//        User usr = userRepo.findOneByUsername(username);

        if(name.length() < 2 || name.length() > 32)
            return false;

        usr.setFirstName(name);
        userRepo.save(usr);

        return true;
    }

    public void setUserRole(User usr, String role) {

        usr.addRole(role);
        userRepo.save(usr);

    }

    public boolean updateUser(User usr){

        User original = userRepo.findOne(usr.getId());

        if(original == null)
            return false;

        //encoding password
        String pwd = usr.getPassword();
        String encoded = passwordEncoder().encode(pwd);
        usr.setPassword(encoded);

        usr.setBalanceChanges(original.getBalanceChanges());
        usr.setRoles(original.getRoles());

        userRepo.save(usr);
        return true;
    }

    /*
    Persistence layer accessing methods below
     */

    public User findUser(String username){
        return userRepo.findOneByUsername(username);
    }

    public User findUser(Long id){
        return userRepo.findOne(id);
    }

    public User findUserByEmail(String email){
        return userRepo.findByEmail(email);
    }

    public void saveUser(User usr){
        userRepo.save(usr);
    }

    public boolean deleteUser(String username){
        User usr = userRepo.findOneByUsername(username);

        if(usr == null)
            return false;

        userRepo.delete(usr);
        return true;
    }

    public boolean deleteUser(Long id){
        User usr = userRepo.findOne(id);

        if(id == null)
            return false;

        userRepo.delete(id);
        return true;
    }

    @PostConstruct
    private void createMockAdminAccount() throws RegistrationException{
        User usr = registerNewUserAccount(new UserDto("adminadmin", "adminadmin", "lol@gmail.com", "krzysztof"));

        usr.setEnabled(true);

        setUserRole(usr, "ROLE_ADMIN");
        setUserRole(usr, "ROLE_USER");
    }



}
