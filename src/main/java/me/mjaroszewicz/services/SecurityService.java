package me.mjaroszewicz.services;

import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.exceptions.RegistrationException;
import me.mjaroszewicz.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepo;

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public String findLoggerInUsername(){

        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();

        if(userDetails instanceof UserDetails)
            return (((UserDetails) userDetails).getUsername());

        return null;
    }

    public void autologin(String username, String password){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        if(usernamePasswordAuthenticationToken.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            log.debug(username + " Logged in successfully");

        }

    }

    public User registerNewUserAccount(UserDto userdto) throws RegistrationException{

        if(!isValidUser(userdto)) throw new RegistrationException();

        User user = new User();
        user.setUsername(userdto.getUsername());
        user.setPassword(passwordEncoder().encode(userdto.getPassword()));



        return userRepo.save(user);

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


}

