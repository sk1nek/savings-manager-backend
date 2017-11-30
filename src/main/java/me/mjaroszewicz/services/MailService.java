package me.mjaroszewicz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    @Autowired
    MailService(){}

    @Bean
    private JavaMailSenderImpl mailSender(){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties props = new Properties();
        props.put("mail.smtp.user", "janekmarcinjanek@gmail.com");
        props.put("mail.smtp.from", "janekmarcinjanek@gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", true);

        mailSender.setPassword("foobarfoobar");

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String subject, String content, String recipient) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(content);
        message.setTo(recipient);

        mailSender.send(message);
    }



}
