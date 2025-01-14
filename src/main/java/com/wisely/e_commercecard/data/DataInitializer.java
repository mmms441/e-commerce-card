package com.wisely.e_commercecard.data;

import com.wisely.e_commercecard.model.Role;
import com.wisely.e_commercecard.model.User;
import com.wisely.e_commercecard.repository.RoleRepository;
import com.wisely.e_commercecard.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRole = Set.of("ROLE_ADMIN" , "ROLE_USER");
            createDefaultUsersIfNotExists();
            createDefaultAdminsIfNotExists();
            createDefaultRoleIfNotExists(defaultRole);
    }
    private void createDefaultUsersIfNotExists(){
        Role adminRole = roleRepository.findByName("ROLE_USER").get();
    for(int i=0 ;i < 5; i++){
        String defaultEmail = "email"+i+"@email.com";
        if(userRepository.existsByEmail(defaultEmail)) {
        continue;}
        User user = new User();
        user.setEmail(defaultEmail);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setFirstName("the user");
        user.setLastName("123456");
        user.setRoles(Set.of(adminRole));
        userRepository.save(user);
        System.out.println("default vet user"+i+"created successfully");
    }
    }

    private void createDefaultAdminsIfNotExists(){
        Role adminrole = roleRepository.findByName("ROLE_ADMIN").get();

        for(int i=0 ;i < 5; i++){
            String defaultEmail = "ADMIN"+i+"@email.com";
            if(userRepository.existsByEmail(defaultEmail)) {
                continue;}

            User user = new User();
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setFirstName("Admin");
            user.setLastName("Admin"+i);
            user.setRoles(Set.of(adminrole));
            userRepository.save(user);
            System.out.println("default admin user"+i+"created successfully");
        }
    }

    public void createDefaultRoleIfNotExists(Set<String> roles){
        roles.stream()
                .filter(role->roleRepository.findByName(role).isEmpty())
                .map(Role::new)
                .forEach(roleRepository::save);


    }
}
