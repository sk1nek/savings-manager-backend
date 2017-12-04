package me.mjaroszewicz.config;

import me.mjaroszewicz.pdf.PDFView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    ProfilePictureStorageProps profilePictureStorageProps(){
        return new ProfilePictureStorageProps();
    }

}
