package me.mjaroszewicz.services;

import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepo;

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

    public User getCurrentUser(){

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

