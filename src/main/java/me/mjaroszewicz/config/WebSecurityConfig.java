package me.mjaroszewicz.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register.html", "/landing", "/css/**", "/user/registration", "/registrationConfirm", "/registrationConfirm/**").permitAll()
//                .anyRequest().authenticated()
                .and()
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN").anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/landing.html", true)
                .failureUrl("/login.html?error=true")
                .permitAll()
                .and()
                .logout().permitAll();
    }

}



