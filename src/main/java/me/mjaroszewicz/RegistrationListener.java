package me.mjaroszewicz;

import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {


    @Bean
    public JavaMailSenderImpl mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setProtocol("SMTP");
        mailSender.setHost("1270.0.01");
        mailSender.setPort(25);

        return mailSender;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent evt){
        this.confirmRegistration(evt);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent evt){
        User usr = evt.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(usr, token);

        String recipientEmail = usr.getEmail();
        String subject = "Registration";
        String confirmationUrl = evt.getAppUrl() + "/registrationConfirm.html?token=" + token;
        String message = messageSource.getMessage("message.recSucc", null, evt.getLocale());

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipientEmail);
        mail.setSubject(subject);
        mail.setText(message + " rn" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(mail);
    }


}
