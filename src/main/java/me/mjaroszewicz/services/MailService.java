package me.mjaroszewicz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
public class MailService {

    @Autowired
    MailService(){}

    @Value("${email.user}")
    private String emailUserName;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.host}")
    private String emailHost;



    @Bean
    private JavaMailSenderImpl mailSender(){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties props = new Properties();
        props.put("mail.smtp.user", emailUserName);
        props.put("mail.smtp.from", emailUserName);
        props.put("mail.smtp.host", emailHost);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", true);

        mailSender.setPassword(emailPassword);

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
