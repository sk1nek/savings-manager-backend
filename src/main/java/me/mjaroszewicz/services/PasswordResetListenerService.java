package me.mjaroszewicz.services;

import me.mjaroszewicz.entities.PasswordResetToken;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.events.OnPasswordResetEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetListenerService implements ApplicationListener<OnPasswordResetEvent> {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(OnPasswordResetEvent evt){
        sendPasswordReset(evt);
    }

    private void sendPasswordReset(OnPasswordResetEvent evt){
        User user = evt.getUser();
        PasswordResetToken passwordResetToken = userService.createPasswordResetToken(user);

        String recipient = user.getEmail();
        String confirmationUrl = evt.getAppUrl() + "/passwordreset.html?token=" + passwordResetToken.getToken();
        String content = "http://localhost:8080" + confirmationUrl;
        mailService.sendEmail("Password reset", content, recipient);

    }
}
