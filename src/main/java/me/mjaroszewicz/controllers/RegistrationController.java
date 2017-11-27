package me.mjaroszewicz.controllers;


import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.exceptions.RegistrationException;
import me.mjaroszewicz.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private SecurityService securityService;

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
