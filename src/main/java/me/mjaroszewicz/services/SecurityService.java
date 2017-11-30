package me.mjaroszewicz.services;

import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.exceptions.RegistrationException;
import me.mjaroszewicz.exceptions.UserNotFoundException;
import me.mjaroszewicz.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class SecurityService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepo;

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

    public User findLoggedInUser(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepo.findOneByUsername(auth.getName());
    }

    public void autologin(String username, String password){

        UserDetails userDetails;
        if(userDetailsService.loadUserByUsername(username) != null)
            userDetails = userDetailsService.loadUserByUsername(username);
        else return;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        if(usernamePasswordAuthenticationToken.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            log.debug(username + " Logged in successfully");

        }

    }






}

