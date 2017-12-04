package me.mjaroszewicz.controllers;


import me.mjaroszewicz.config.ProfilePictureStorageProps;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.exceptions.StorageException;
import me.mjaroszewicz.services.AnnouncingService;
import me.mjaroszewicz.services.ProfilePictureStorageService;
import me.mjaroszewicz.services.SecurityService;
import me.mjaroszewicz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private AnnouncingService announcingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfilePictureStorageService profileService;

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

    @PostMapping("/announce")
    public ResponseEntity<String> sendAnnouncement(@RequestParam(required = true) String announcement){

        announcingService.publishAnnouncement(announcement);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/uploaduserpicture")
    public ResponseEntity<String> uploadUserProfilePic(@RequestParam(required = true)MultipartFile file, String username){

        try{
            profileService.storeProfilePic(file, username);
        }catch(StorageException sex){
            return new ResponseEntity<>(sex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/deleteuserpicture")
    public ResponseEntity<String> removeUserProfilePic(@RequestParam(required = true) String username){

        if(profileService.deleteUserPic(userService.findUser(username)))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
