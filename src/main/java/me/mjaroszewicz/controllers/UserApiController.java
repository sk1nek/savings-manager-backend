package me.mjaroszewicz.controllers;

import me.mjaroszewicz.dtos.BalanceChangeDto;
import me.mjaroszewicz.entities.BalanceChange;
import me.mjaroszewicz.entities.User;
import me.mjaroszewicz.repositories.UserRepository;
import me.mjaroszewicz.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = {"", "/"})
    public User getCurrentUser() {
        return securityService.findLoggedInUser().getSanitizedUser();
    }

    @PostMapping("/addbalancechange")
    public void addBalanceChange(@RequestBody BalanceChangeDto dto){

        BalanceChange bc = new BalanceChange();
        bc.setDetails(dto.getDetails());
        bc.setExpense(dto.isExpense());
        bc.setTitle(dto.getTitle());
        bc.setAmount(dto.getValue());
        bc.setTimestamp(System.currentTimeMillis());

        User usr = getCurrentUser();
        usr.addBalanceChange(bc);
        userRepo.save(usr);
    }

    @PostMapping("/removebalancechange")
    public void removeBalanceChange(@RequestParam Long id){

        User usr = getCurrentUser();
        usr.removeBalanceChange(id);
        userRepo.save(usr);

    }
}
