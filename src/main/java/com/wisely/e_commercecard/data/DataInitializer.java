package com.wisely.e_commercecard.data;

import com.wisely.e_commercecard.model.User;
import com.wisely.e_commercecard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
            createDefaultUsersIfNotExists();
    }
    private void createDefaultUsersIfNotExists(){

    for(int i=0 ;i < 5; i++){
        String defaultEmail = "email"+i+"@email.com";
        if(userRepository.existsByEmail(defaultEmail)) {
        continue;}
        User user = new User();
        user.setEmail(defaultEmail);
        user.setPassword("password"+i);
        user.setFirstName("the user");
        user.setLastName("123456");
        userRepository.save(user);
        System.out.println("default vet user"+i+"created successfully");
    }
    }
}
