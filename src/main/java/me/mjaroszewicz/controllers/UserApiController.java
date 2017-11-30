package me.mjaroszewicz.controllers;

import me.mjaroszewicz.dtos.BalanceChangeDto;
import me.mjaroszewicz.entities.BalanceChange;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.repositories.UserRepository;
import me.mjaroszewicz.services.SecurityService;
import me.mjaroszewicz.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @GetMapping(value = {"", "/"})
    public User getCurrentUser() {
        return securityService.findLoggedInUser().getSanitizedUser();
    }

    @PostMapping("/addbalancechange")
    public ResponseEntity<String> addBalanceChange(@RequestBody BalanceChangeDto dto){

        BalanceChange bc = new BalanceChange();
        bc.setDetails(dto.getDetails());
        bc.setExpense(dto.isExpense());
        bc.setTitle(dto.getTitle());
        bc.setAmount(dto.getValue());
        bc.setTimestamp(System.currentTimeMillis());

        User usr = getCurrentUser();
        usr.addBalanceChange(bc);
        userRepo.save(usr);

        return new ResponseEntity<>("Item successfully added", HttpStatus.OK);
    }

    @PostMapping("/removebalancechange")
    public ResponseEntity<String>removeBalanceChange(@RequestParam Long id){

        User usr = getCurrentUser();
        usr.removeBalanceChange(id);
        userRepo.save(usr);

        return new ResponseEntity<>("Item successfully removed", HttpStatus.OK);
    }

    /**
     * @param password new password, must be at least 8 characters long
     * @return HTTP status 406 if password is too short, 200 if operation was succesful
     */
    @PostMapping("/changepassword")
    public ResponseEntity<String> changeUserPassword(@Payload String password) {

        if(userService.changeCurrentUserPassword(password)){
            return new ResponseEntity<>("Password too short", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>("Password successfully changed", HttpStatus.OK);

    }
}
