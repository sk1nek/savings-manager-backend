package me.mjaroszewicz.controllers;

import me.mjaroszewicz.events.OnRegistrationCompleteEvent;
import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.entities.VerificationToken;
import me.mjaroszewicz.exceptions.RegistrationException;
import me.mjaroszewicz.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @PostMapping("/user/registration")
    public ResponseEntity<String> registerUserAccount(
            @RequestBody UserDto userDto, WebRequest request){

        try{
            String appUrl = request.getContextPath();
            User registered = userService.registerNewUserAccount(userDto);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));

        }catch(RegistrationException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>("Account successfully registered, check your mailbox for confirmation e-mail.", HttpStatus.OK);
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<String> confirmRegistration(
            WebRequest request, Model mdl,
            @RequestParam String token){

        VerificationToken vt = userService.getVerificationToken(token);

        if(vt == null){
            return new ResponseEntity<>("Invalid token.", HttpStatus.NOT_FOUND);
        }

        User usr = vt.getUser();
        Calendar cal = Calendar.getInstance();

        if((vt.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0){
            return new ResponseEntity<>("Verification token expired", HttpStatus.NOT_ACCEPTABLE);
        }

        usr.setEnabled(true);
        userService.saveRegisteredUser(usr);

        return new ResponseEntity<>("E-mail succesfully confirmed", HttpStatus.OK);

    }

    @GetMapping("/resendconfirmation")
    public ResponseEntity<String> resendConfirmation(
            @RequestParam("username") String username,
            WebRequest request){

        User usr;

        if((usr = userService.findUser(username)) == null)
            return new ResponseEntity<>("Invalid username.", HttpStatus.NOT_ACCEPTABLE);

        if(usr.isEnabled())
            return new ResponseEntity<>("User already active.", HttpStatus.NOT_ACCEPTABLE);

        userService.sendVerificationMail(usr, request.getContextPath());

        return new ResponseEntity<>("Confirmation email successfully re-sent", HttpStatus.OK);

    }
}
