package me.mjaroszewicz.controllers;


import me.mjaroszewicz.entities.User;
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
    public ResponseEntity<Object> getUserById(
            @RequestParam("id") Long id) {

        User usr = userService.findUser(id);

        if(usr == null)
            return new ResponseEntity<>("There is no such user", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(usr, HttpStatus.FOUND);
    }

    @PostMapping("/getuserbyname")
    public ResponseEntity<Object> getUserByName(
            @RequestParam("name") String name){

        User usr = userService.findUser(name);

        if(usr == null)
            return new ResponseEntity<>("THere is no such user", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(usr, HttpStatus.FOUND);
    }

    @DeleteMapping("/deleteuser")
    public ResponseEntity<String> deleteUser(
            @RequestParam(value = "byname", required = true) Boolean byName,
            @RequestParam(value = "identifier", required = true) String identifier) {

        if (byName && userService.deleteUser(identifier))
            return new ResponseEntity<>("User deleted successfully. ", HttpStatus.OK);
        else if (!byName && userService.deleteUser(Long.parseLong(identifier)))
            return new ResponseEntity<>("User deleted successfully. ", HttpStatus.OK);


        return new ResponseEntity<>("Could not delete specified user. ", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/updateuser")
    public ResponseEntity<String> updateUser(@RequestBody User user) {

        if(userService.updateUser(user))
            return new ResponseEntity<>("User successfully updated", HttpStatus.OK);

        return new ResponseEntity<>("User not found. ", HttpStatus.NOT_FOUND);
    }

}
