package me.mjaroszewicz.events;

import me.mjaroszewicz.entities.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent{

    private User user;

    private String appUrl;

    public OnRegistrationCompleteEvent(Object src, User user, String appUrl){
        super(src);

        this.user = user;
        this.appUrl = appUrl;
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
