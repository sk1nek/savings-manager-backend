package me.mjaroszewicz.controllers;


import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.exceptions.StorageException;
import me.mjaroszewicz.services.AnnouncingService;
import me.mjaroszewicz.services.ProfilePictureStorageService;
import me.mjaroszewicz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private AnnouncingService announcingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfilePictureStorageService profileService;

    /**
     * @param id requested user id
     * @return Requested user represented as Json string.
     */
    @PostMapping("/getuserbyid")
    public ResponseEntity<Object> getUserById(
            @RequestParam("id") Long id) {

        User usr = userService.findUser(id);

        if(usr == null)
            return new ResponseEntity<>("There is no such user", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(usr, HttpStatus.FOUND);
    }

    /**
     *
     * @param name requested user name
     * @return Requested user represented as Json string.
     */
    @PostMapping("/getuserbyname")
    public ResponseEntity<Object> getUserByName(
            @RequestParam("name") String name){

        User usr = userService.findUser(name);

        if(usr == null)
            return new ResponseEntity<>("THere is no such user", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(usr, HttpStatus.FOUND);
    }

    /**
     * Deletes specified user either by name or numerical ID (byName == false)
     * @param byName Boolean flag specifying if username should be used as identifier. If it's false, ID is numerical.
     * @param identifier String used to identify user - either username or numerical ID according to byName flag
     * @return ResponseEntity with HttpStatus - 200 on success / 404 on fail
     */
    @DeleteMapping("/deleteuser")
    public ResponseEntity<String> deleteUser(
            @RequestParam(value = "byname") Boolean byName,
            @RequestParam(value = "identifier") String identifier) {

        if (byName && userService.deleteUser(identifier))
            return new ResponseEntity<>("User deleted successfully. ", HttpStatus.OK);
        else if (!byName && userService.deleteUser(Long.parseLong(identifier)))
            return new ResponseEntity<>("User deleted successfully. ", HttpStatus.OK);


        return new ResponseEntity<>("Could not delete specified user. ", HttpStatus.NOT_FOUND);
    }

    /**
     * Looks into persistance layer if username with same id/username exists. If yes, existing one is updated with details from arg.
     * @param user
     * @return HTTP 200 / 404 depending on result
     */
    @PatchMapping("/updateuser")
    public ResponseEntity<String> updateUser(@RequestBody User user) {

        if(userService.updateUser(user))
            return new ResponseEntity<>("User successfully updated", HttpStatus.OK);

        return new ResponseEntity<>("User not found. ", HttpStatus.NOT_FOUND);
    }

    /**
     * Sends message to all sockets subscribed to /announcement
     * @param announcement - Message to be announced
     * @return HTTP 200 on success
     */
    @PostMapping("/announce")
    public ResponseEntity<String> sendAnnouncement(@RequestParam() String announcement){

        announcingService.publishAnnouncement(announcement);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Uploads new user-specific profile picture.
     *
     * @param file File containing new profile picture
     * @param username Name of user to have picture changed
     * @return HTTP 200 on success / 406 on fail
     */
    @PostMapping("/uploaduserpicture")
    public ResponseEntity<String> uploadUserProfilePic(@RequestParam MultipartFile file, String username){

        try{
            profileService.storeProfilePic(file, username);
        }catch(StorageException sex){
            return new ResponseEntity<>(sex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Deletes profile picture of user with username specified in arg
     *
     * @param username - Target user name
     * @return HTTP 200 or 404 whether operation succeeds or not
     */
    @PostMapping("/deleteuserpicture")
    public ResponseEntity<String> removeUserProfilePic(@RequestParam String username){

        if(profileService.deleteUserPic(userService.findUser(username)))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @return all users in list.
     */
    @GetMapping
    public ResponseEntity<ArrayList<User>> getAllUsers(){

        ArrayList<User> query = userService.findAllUsers();

        return new ResponseEntity<ArrayList<User>>(query, HttpStatus.FOUND);
    }

}
