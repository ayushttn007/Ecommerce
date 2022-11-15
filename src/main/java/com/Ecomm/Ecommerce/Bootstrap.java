package com.Ecomm.Ecommerce;

import com.Ecomm.Ecommerce.entities.Role;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.repos.RoleRepo;
import com.Ecomm.Ecommerce.repos.UserRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationRunner {

    protected final Log logger = LogFactory.getLog(getClass());

    private UserRepo userRepository;

    private RoleRepo roleRepository;

    @Autowired
    public Bootstrap(UserRepo userRepository, RoleRepo roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("BootstrapCommandLineRunner's run method started.");

        if(roleRepository.count()<1){
            Role role1 = new Role();
            role1.setId(1);
            role1.setAuthority("ADMIN");

            Role role2 = new Role();
            role2.setId(2);
            role2.setAuthority("SELLER");

            Role role3 = new Role();
            role3.setId(3);
            role3.setAuthority("CUSTOMER");

            roleRepository.save(role1);
            roleRepository.save(role2);
            roleRepository.save(role3);

        }
        if(userRepository.count()<1) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user = new User();
            user.setFirstName("root");
            user.setLastName("User");
            user.setEmail("ayush@gmail.com");
            user.setPassword(passwordEncoder.encode("ecommsecret"));
            Role role = roleRepository.findById(1L).get();
            user.setRole(role);
            userRepository.save(user);
        }

        logger.info("populated Roles table and created Admin user");
    }
}