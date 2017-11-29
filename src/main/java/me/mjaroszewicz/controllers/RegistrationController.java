package me.mjaroszewicz.controllers;


import me.mjaroszewicz.OnRegistrationCompleteEvent;
import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.entities.VerificationToken;
import me.mjaroszewicz.exceptions.RegistrationException;
import me.mjaroszewicz.services.SecurityService;
import me.mjaroszewicz.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.Locale;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private MessageSource messages;

    @PostMapping("/user/registration")
    public ResponseEntity<String> registerUserAccount(@RequestBody UserDto userDto, BindingResult result, WebRequest request, Errors err){

        log.info("Registering");

        ResponseEntity<String> ret = new ResponseEntity<>(HttpStatus.ACCEPTED);
        try{
            String appUrl = request.getContextPath();
            User registered = userService.registerNewUserAccount(userDto);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        }catch(RegistrationException ex){
            log.info("hah");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }


        return ret;

    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, Model mdl, @RequestParam String token){

        Locale loc = request.getLocale();

        VerificationToken vt = userService.getVerificationToken(token);

        if(vt == null){
            //handle invalid token
        }

        User usr = vt.getUser();
        Calendar cal = Calendar.getInstance();

        if((vt.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0){
            //handle expired token
        }

        usr.setEnabled(true);
        userService.saveRegisteredUser(usr);

        return "redirect:/landing.html";

    }

}
