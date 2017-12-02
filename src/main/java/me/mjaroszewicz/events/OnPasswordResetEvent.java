package me.mjaroszewicz.events;

import me.mjaroszewicz.entities.User;
import org.springframework.context.ApplicationEvent;

public class OnPasswordResetEvent extends ApplicationEvent {

    private User user;

    private String appUrl;

    public OnPasswordResetEvent(Object src, User user, String appUrl) {
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
