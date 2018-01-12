package me.mjaroszewicz.controllers;

import me.mjaroszewicz.annotations.ValidUserDto;
import me.mjaroszewicz.events.OnRegistrationCompleteEvent;
import me.mjaroszewicz.dtos.UserRegistrationDto;
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
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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

    /**
     *
     * @param userRegistrationDto Data Transfer Object containing registration credentials
     * @param err Validation errors provided by Spring
     * @return HTTP 200 on success, 406 and error message if any difficulties were to be encountered
     */
    @PostMapping("/user/registration")
    public ResponseEntity<String> registerUserAccount(
            @RequestParam("user") @ValidUserDto UserRegistrationDto userRegistrationDto, WebRequest request, Errors err){

        if(err.hasErrors())
            return new ResponseEntity<>("Invalid credentials", HttpStatus.NOT_ACCEPTABLE);

        if(userService.userExists(userRegistrationDto.getUsername()))
            return new ResponseEntity<>("Username already exists", HttpStatus.NOT_ACCEPTABLE);

        try{
            String appUrl = request.getContextPath();
            User registered = userService.registerNewUserAccount(userRegistrationDto);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(this, registered, appUrl));

        }catch(RegistrationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>("Account successfully registered, check your mailbox for confirmation e-mail.", HttpStatus.OK);
    }

    /**
     * Confirms user e-mail address and enables account if provided token is valid
     * @param token - token sent in e-mail on registration
     * @return HTTP 200 on success, 406/404 and a message on fail.
     */
    @GetMapping("/registrationConfirm")
    public ResponseEntity<String> confirmRegistration(
            @RequestParam String token){

        VerificationToken vt = userService.getVerificationToken(token);

        if(vt == null)
            return new ResponseEntity<>("Invalid token.", HttpStatus.NOT_FOUND);

        User usr = vt.getUser();
        Calendar cal = Calendar.getInstance();

        if((vt.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0){
            return new ResponseEntity<>("Verification token expired", HttpStatus.NOT_ACCEPTABLE);
        }

        usr.setEnabled(true);
        userService.saveRegisteredUser(usr);

        return new ResponseEntity<>("E-mail succesfully confirmed", HttpStatus.OK);

    }

    /**
     * Re-sends user confirmation email.
     *
     * @param username Target user name
     * @param request Provided by Spring
     * @return HTTP 200 on success, 406 and an error on fail
     */
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
