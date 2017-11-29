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


import java.util.Properties;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {


    @Bean
    public JavaMailSenderImpl mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", true);

        mailSender.setProtocol("smtp");
        mailSender.setHost("smtp.gmail.com");
        mailSender.setUsername("janekmarcinjanek@gmail.com");
        mailSender.setPassword("foobarfoobar");
        mailSender.setJavaMailProperties(props);

        mailSender.setPort(587);


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
        String message = "msg"; //fix dis

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("skinek1942@gmail.com");
        mail.setTo(recipientEmail);
        mail.setSubject(subject);
        mail.setText("http://localhost:8080" + confirmationUrl);
        mailSender.send(mail);
        System.out.println("Sending");
    }


}
