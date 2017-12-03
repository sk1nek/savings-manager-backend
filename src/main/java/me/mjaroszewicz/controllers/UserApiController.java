package me.mjaroszewicz.controllers;

import me.mjaroszewicz.dtos.BalanceChangeDto;
import me.mjaroszewicz.entities.BalanceChange;
import me.mjaroszewicz.entities.PasswordResetToken;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.events.OnPasswordResetEvent;
import me.mjaroszewicz.pdf.PDFView;
import me.mjaroszewicz.services.SecurityService;
import me.mjaroszewicz.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
     * @return HTTP status 406 if password is too short, 200 if operation was successful
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
                return new ResponseEntity<>("Name length should be between 3 and 32 characters.", HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>("First name changed to " + value + ".", HttpStatus.OK);
    }


    @PostMapping("/forgotpassword")
    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email, WebRequest request) {

        User usr;

        if((usr = userService.findUserByEmail(email)) == null)
            return new ResponseEntity<>("Invalid e-mail, try again. ", HttpStatus.NOT_FOUND);
        else
            eventPublisher.publishEvent(new OnPasswordResetEvent(this, usr, request.getContextPath()));

        return new ResponseEntity<>("E-mail has been sent. Check your inbox. ", HttpStatus.OK);
    }

    @PostMapping("/passwordreset")
    public ResponseEntity<String> userPasswordReset(
            @RequestParam("token") String token,
            @RequestParam("password") String password){

        PasswordResetToken passwordResetToken;

        if ((passwordResetToken = userService.getPasswordResetToken(token)) != null) {
            userService.changeUserPassword(passwordResetToken.getUser(), password);
            return new ResponseEntity<>("Password successfully changed. ", HttpStatus.OK);
        }

        return new ResponseEntity<>("Could not change password. (Token invalid or expired)", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/pdf")
    public ModelAndView downloadPDF(){

        ArrayList<BalanceChange> list = new ArrayList<>();

        list.add(new BalanceChange("title", "details", 500L, true, 15L));

        ModelAndView ret = new ModelAndView();

        return new ModelAndView(new PDFView(), "items", list);
    }




}
