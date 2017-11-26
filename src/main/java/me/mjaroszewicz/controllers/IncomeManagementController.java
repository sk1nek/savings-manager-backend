package me.mjaroszewicz.controllers;


import me.mjaroszewicz.dtos.UserDto;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IncomeManagementController {

    @Autowired
    UserRepository userRepo;

    @GetMapping("/test")
    public User getTest(){
        return userRepo.findOneByUsername("username");
    }
}
