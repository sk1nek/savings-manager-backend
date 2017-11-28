package me.mjaroszewicz;

import me.mjaroszewicz.entities.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent{


    private Locale locale;

    private User user;

    private String appUrl;

    public OnRegistrationCompleteEvent(
            User user, Locale locale, String appUrl){
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;

    }


    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}
