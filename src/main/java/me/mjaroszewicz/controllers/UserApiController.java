package me.mjaroszewicz.controllers;

import me.mjaroszewicz.annotations.ValidFirstName;
import me.mjaroszewicz.annotations.ValidPassword;
import me.mjaroszewicz.dtos.BalanceChangeDto;
import me.mjaroszewicz.entities.BalanceChange;
import me.mjaroszewicz.entities.PasswordResetToken;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.events.OnPasswordResetEvent;
import me.mjaroszewicz.exceptions.StorageException;
import me.mjaroszewicz.pdf.PDFView;
import me.mjaroszewicz.services.ProfilePictureStorageService;
import me.mjaroszewicz.services.SecurityService;
import me.mjaroszewicz.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
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

    @Autowired
    private ProfilePictureStorageService profilePictureStorageService;

    @GetMapping(value = {"", "/"})
    public User getCurrentUser() {
        User usr = securityService.getCurrentUser();
        usr.setPassword(null);
        return usr;
    }

    @PostMapping("/addbalancechange")
    public ResponseEntity<String> addBalanceChange(@RequestBody BalanceChangeDto dto) {

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
    public ResponseEntity<String> removeBalanceChange(@RequestParam Long id) {

        User usr = getCurrentUser();
        usr.removeBalanceChange(id);
        userService.saveUser(usr);

        return new ResponseEntity<>("Item successfully removed", HttpStatus.OK);
    }

    @PostMapping("/setMonthlyBudget")
    public ResponseEntity<String> setMonthlyBudget(@RequestParam Long budget){

        User usr = securityService.getCurrentUser();
        usr.setMonthlyBudget(budget);

        userService.updateUser(usr);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getmonthlybudget")
    public ResponseEntity<Long> getMonthlyBudget(){

        User usr = securityService.getCurrentUser();
        Long ret = usr.getMonthlyBudget();

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }


    /**
     * @param password new password, must be at least 8 characters long
     * @return HTTP status 406 if password is too short, 200 if operation was successful
     */
    @PostMapping("/changepassword")
    public ResponseEntity<String> changeUserPassword(@Payload @ValidPassword String password, Errors err) {

        if(err.hasErrors())
            return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);

        if (userService.changeCurrentUserPassword(password)) {
            return new ResponseEntity<>("Password too short", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>("Password successfully changed", HttpStatus.OK);
    }

    @PostMapping("/changefirstname")
    public ResponseEntity<String> changeUserFirstName(@RequestParam("value") @ValidFirstName String value, Errors err) {

        if(err.hasErrors())
            return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);

        if (!userService.changeUserFirstName(securityService.getCurrentUser(), value))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>("First name changed to " + value + ".", HttpStatus.OK);
    }


    @PostMapping("/forgotpassword")
    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email, WebRequest request) {

        User usr;

        if ((usr = userService.findUserByEmail(email)) == null)
            return new ResponseEntity<>("Invalid e-mail, try again. ", HttpStatus.NOT_FOUND);
        else
            eventPublisher.publishEvent(new OnPasswordResetEvent(this, usr, request.getContextPath()));

        return new ResponseEntity<>("E-mail has been sent. Check your inbox. ", HttpStatus.OK);
    }

    @PostMapping("/passwordreset")
    public ResponseEntity<String> userPasswordReset(@RequestParam("token") String token, @RequestParam("password") @ValidPassword String password, Errors err) {

        if(err.hasErrors())
            return new ResponseEntity<String>("Invalid password", HttpStatus.NOT_ACCEPTABLE);

        PasswordResetToken passwordResetToken;

        if ((passwordResetToken = userService.getPasswordResetToken(token)) != null) {
            userService.changeUserPassword(passwordResetToken.getUser(), password);
            return new ResponseEntity<>("Password successfully changed. ", HttpStatus.OK);
        }

        return new ResponseEntity<>("Could not change password. (Token invalid or expired)", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/pdf")
    public ModelAndView downloadPDF() {

        ModelAndView ret = new ModelAndView();

        ArrayList<BalanceChange> list = new ArrayList<>();
        list.addAll(securityService.getCurrentUser().getBalanceChanges());

        return new ModelAndView(new PDFView(), "items", list);
    }

    @PostMapping("/uploadprofilepic")
    public ResponseEntity<String> handleProfilePicUpload(@RequestParam("file") MultipartFile file) throws StorageException{

        User usr = securityService.getCurrentUser();

        profilePictureStorageService.storeProfilePic(file, usr.getUsername());

        return new ResponseEntity<>("Upload succesful. ", HttpStatus.OK);
    }

    @GetMapping("/getprofilepicture")
    @ResponseBody
    public ResponseEntity<Resource> serveProfilePicture() throws StorageException{

        User usr = securityService.getCurrentUser();
        Resource resource = profilePictureStorageService.loadAsResource(usr.getUsername());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException ex){
        return ResponseEntity.notFound().build();
    }


}
