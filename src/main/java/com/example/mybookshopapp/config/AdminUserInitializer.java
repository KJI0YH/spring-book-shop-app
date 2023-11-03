package com.example.mybookshopapp.config;

import com.example.mybookshopapp.data.RoleEntity;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.services.RoleService;
import com.example.mybookshopapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminUserInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleService roleService;
    private final UserService userService;
    private final ConfigurableApplicationContext applicationContext;
    
    @Value("${config.admin.name}")
    private String adminName;
    @Value("${config.admin.email}")
    private String adminEmail;
    @Value("${config.admin.phone}")
    private String adminPhone;
    @Value("${config.admin.password}")
    private String adminPassword;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        RoleEntity adminRole = roleService.getRoleByName("ADMIN");
        if (adminRole != null && roleService.getNumberOfUsersByRole(adminRole) == 0) {
            try {
                userService.registerNewUser(new RegistrationForm(adminName, adminEmail, adminPhone, adminPassword), "ADMIN");
            } catch (Exception e) {

                // Error creating admin user
                SpringApplication.exit(applicationContext, (ExitCodeGenerator) () -> 1);
            }
        }
    }
}
