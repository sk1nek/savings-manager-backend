package me.mjaroszewicz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    ProfilePictureStorageProps profilePictureStorageProps(){
        return new ProfilePictureStorageProps();
    }

}
