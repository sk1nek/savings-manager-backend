package me.mjaroszewicz;

import me.mjaroszewicz.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockEntityPopulator {

    @Autowired
    UserRepository userRepo;
}
