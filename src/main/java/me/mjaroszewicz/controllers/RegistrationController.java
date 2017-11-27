package me.mjaroszewicz.controllers;


import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.exceptions.RegistrationException;
import me.mjaroszewicz.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.executable.ValidateOnExecution;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private SecurityService securityService;
//
//    @GetMapping("/user/registration")
//    public String getRegistratioNForm(WebRequest request, Model mdl){
//        UserDto userDto = new UserDto();
//        mdl.addAttribute("user", userDto);
//        return "registration";
//    }

    @PostMapping("/user/registration")
    public ResponseEntity<String> registerUserAccount(@RequestBody UserDto userDto, BindingResult result, WebRequest request, Errors err){

        ResponseEntity<String> ret = new ResponseEntity<>(HttpStatus.ACCEPTED);
        try{
            securityService.registerNewUserAccount(userDto);
        }catch(RegistrationException ex){
            log.info("hah");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        }

        return ret;

    }

}
