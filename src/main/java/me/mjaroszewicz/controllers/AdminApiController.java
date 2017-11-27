package me.mjaroszewicz.controllers;


import me.mjaroszewicz.repositories.UserRepository;
import me.mjaroszewicz.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserRepository userRepo;


}
