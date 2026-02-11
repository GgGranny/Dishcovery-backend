package com.dishcovery.backend.components;

import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitailizer implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String ...args) {
        if (userRepo.findByUsername("admin") == null) {
            Users admin = new Users();
            admin.setUsername("admin");
            admin.setEmail("admin@dishcovery.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            admin.setDisplayName("Administrator");
            admin.setRecipeCount(0);
            admin.setFollowersCount(0);

            userRepo.save(admin);

            System.out.println("✅ Admin created!");
            System.out.println("Username: admin");
            System.out.println("Password: Admin@123");
        }
    }

}
