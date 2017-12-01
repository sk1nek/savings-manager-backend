package me.mjaroszewicz.controllers;

import me.mjaroszewicz.dtos.BalanceChangeDto;
import me.mjaroszewicz.entities.BalanceChange;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.services.SecurityService;
import me.mjaroszewicz.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @GetMapping(value = {"", "/"})
    public User getCurrentUser() {
        User usr = securityService.getCurrentUser();
        usr.setPassword(null);
        return usr;
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
        userService.saveUser(usr);

        return new ResponseEntity<>("Item successfully added", HttpStatus.OK);
    }

    @PostMapping("/removebalancechange")
    public ResponseEntity<String>removeBalanceChange(@RequestParam Long id){

        User usr = getCurrentUser();
        usr.removeBalanceChange(id);
        userService.saveUser(usr);

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

    /**
     * @param value new name
     * @return 406 if new name is either too short or too long (accepted length is 3-32 characters)
     */
    @PostMapping("/changefirstname")
    public ResponseEntity<String> changeUserFirstName(@RequestParam("value") String value) {

            if(!userService.changeUserFirstName(securityService.getCurrentUser(), value))
                return new ResponseEntity<String>("Name length should be between 3 and 32 characters.", HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<String>("First name changed to " + value + ".", HttpStatus.OK);
    }


}
