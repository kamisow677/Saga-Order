package com.kamillo.task.scheduler;

import com.kamillo.task.scheduler.domain.CustomeUserService;
import com.kamillo.task.scheduler.security.users.CustomeRole;
import com.kamillo.task.scheduler.security.users.CustomeUser;
import com.kamillo.task.scheduler.security.users.repo.CustomeUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

@SpringBootApplication
@ActiveProfiles("local")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


//	@Bean
//	CommandLineRunner run(CustomeUserService customeUserSevice) {
//		return args -> {
//			customeUserSevice.saveCustomeRole(CustomeRole.builder().name(CustomeRole.ROLE.ROLE_USER).build());
//			customeUserSevice.saveCustomeRole(CustomeRole.builder().name(CustomeRole.ROLE.ROLE_MANAGER).build());
//			customeUserSevice.saveCustomeRole(CustomeRole.builder().name(CustomeRole.ROLE.ROLE_ADMIN).build());
//			customeUserSevice.saveCustomeRole(CustomeRole.builder().name(CustomeRole.ROLE.ROLE_SUPER_ADMIN).build());
//			customeUserSevice.saveCustomeUser(
//					CustomeUser.builder().username("John Smith").username("jim").password("password").roles(new HashSet<>()).build()
//			);
//			customeUserSevice.saveCustomeUser(
//					CustomeUser.builder().username("Kaczor Donald").username("donald").password("password").roles(new HashSet<>()).build()
//			);
//			customeUserSevice.saveCustomeUser(
//					CustomeUser.builder().username("Tom Sawyer").username("tommy").password("password").roles(new HashSet<>()).build()
//			);
//			customeUserSevice.saveCustomeUser(
//					CustomeUser.builder().username("Don Corleone").username("don").password("password").roles(new HashSet<>()).build()
//			);
//
//			customeUserSevice.addRoleToUser("jim", CustomeRole.ROLE.ROLE_USER);
//			customeUserSevice.addRoleToUser("jim", CustomeRole.ROLE.ROLE_MANAGER);
//			customeUserSevice.addRoleToUser("donald", CustomeRole.ROLE.ROLE_MANAGER);
//			customeUserSevice.addRoleToUser("tommy", CustomeRole.ROLE.ROLE_ADMIN);
//			customeUserSevice.addRoleToUser("don", CustomeRole.ROLE.ROLE_SUPER_ADMIN);
//			customeUserSevice.addRoleToUser("don", CustomeRole.ROLE.ROLE_ADMIN);
//			customeUserSevice.addRoleToUser("don", CustomeRole.ROLE.ROLE_USER);
//		};
//	}

}
