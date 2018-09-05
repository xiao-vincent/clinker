package com.vince.retailmanager.demo;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.User;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.repository.FranchisorRepository;
import com.vince.retailmanager.repository.UserRepository;
import com.vince.retailmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InsertDemoData {
    private static final String[] usernames = {"franchisorArbys", "franchiseeArbys"};

    private Map<String, User> userMap;
    private List<Franchisor> franchisors;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private FranchisorRepository franchisorRepo;

    @Autowired
    private FranchiseeRepository franchiseeRepo;

    @EventListener
    @Transactional
    public void appReady(ApplicationReadyEvent event) throws Exception {
        System.out.println("Setting up demo data...");
        clearDBTables();
        createFranchise("arbys", "Arby's", "fastfood", 2);
        User admin = new User("admin", "adminpassword");
        admin.addRole("ADMIN");
        userService.saveUser(admin);


        System.out.println("\nDemo data setup complete!");

    }

    private void clearDBTables() {
        userRepo.deleteAll();
        franchisorRepo.deleteAll();
        franchiseeRepo.deleteAll();
    }

    private void createUsers() {
        Map<String, User> users = new HashMap<>();
        for (String username :
                usernames) {
            users.put(username, new User(username, "password"));
        }
        userMap = users;
    }

    private void createFranchisors() {
        franchisors = new ArrayList<>();
        Franchisor arbys = new Franchisor("Arby's", "arbys.com", "fast-food");
        arbys.setUser(userMap.get("franchisorArbys"));

        franchisors.add(arbys);

    }

    public void createFranchise(String shortCode, String brandName, String industry, int numOfFranchisees) throws Exception {
        //setup franchisor
        User franchisorUser = new User(shortCode + "_franchisor", "password");
        userService.saveUser(franchisorUser);
        Franchisor franchisor = new Franchisor(brandName, brandName + ".com", industry);
        franchisor.setUser(franchisorUser);

        franchisorRepo.save(franchisor);

        //setup franchisee
        List<Franchisee> franchisees = new ArrayList<>();
        for (int i = 1; i <= numOfFranchisees; i++) {
            Franchisee franchisee = new Franchisee();
            franchisee.setFranchisor(franchisor);
            User franchiseeUser = new User(shortCode + "_franchisee_" + i, "password");
            franchisee.setUser(franchiseeUser);
            franchisees.add(new Franchisee());
            userService.saveUser(franchiseeUser);
            franchiseeRepo.save(franchisee);
        }

    }
}
