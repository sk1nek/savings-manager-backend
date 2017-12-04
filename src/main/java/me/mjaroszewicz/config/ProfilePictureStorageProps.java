package me.mjaroszewicz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("profilepicproperties")
public class ProfilePictureStorageProps {

    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
