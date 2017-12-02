package me.mjaroszewicz.services;

import me.mjaroszewicz.events.OnRegistrationCompleteEvent;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.entities.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class RegistrationListenerService implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent evt){
        confirmRegistration(evt);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent evt){
        User usr = evt.getUser();
        VerificationToken token = userService.createVerificationToken(usr);

        String recipientEmail = usr.getEmail();
        String confirmationUrl = evt.getAppUrl() + "/registrationConfirm.html?token=" + token.getToken();
        String content = "http://localhost:8080" + confirmationUrl;
        mailService.sendEmail("Registration", content, recipientEmail);
    }


}
