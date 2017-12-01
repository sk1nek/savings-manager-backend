package me.mjaroszewicz.controllers;


import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.repositories.UserRepository;
import me.mjaroszewicz.services.SecurityService;
import me.mjaroszewicz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @PostMapping("/getuserbyid")
    public ResponseEntity<Object> getUserById(@RequestParam("id") Long id) {

        User usr = userService.findUser(id);

        if(usr == null)
            return new ResponseEntity<>("There is no such user", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(usr, HttpStatus.FOUND);
    }

    @PostMapping("/getuserbyname")
    public ResponseEntity<Object> getUserByName(@RequestParam("name") String name){

        User usr = userService.findUser(name);

        if(usr == null)
            return new ResponseEntity<>("THere is no such user", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(usr, HttpStatus.FOUND);
    }

}
