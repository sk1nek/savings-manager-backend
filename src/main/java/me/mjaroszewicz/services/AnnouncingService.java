package me.mjaroszewicz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnnouncingService {

    private final static Logger log = LoggerFactory.getLogger(AnnouncingService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void publishAnnouncement(String message){
        log.debug("Sending an announcement: " + message);
        messagingTemplate.convertAndSend("/announcements", message);
    }



}
