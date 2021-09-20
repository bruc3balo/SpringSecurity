package com.security.spring;

import com.security.spring.api.domain.AppRole;
import com.security.spring.api.domain.AppUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;

import static com.security.spring.global.GlobalService.dataService;

@SpringBootApplication
@EnableJpaRepositories
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

/*	@Bean
	CommandLineRunner run () {
		return args -> {
			//roles
			dataService.saveARole(new AppRole("ROLE_USER"));
			dataService.saveARole(new AppRole("ROLE_ADMIN"));
			dataService.saveARole(new AppRole("ROLE_SUPER_ADMIN"));

			//Users
			dataService.saveAUser(new AppUser("Superman", "Kal-El", "Clark@kanzas.com", "Lois", new ArrayList<>()));
			dataService.saveAUser(new AppUser("Batman", "Wayne", "richboi@gotham.com", "Robin", new ArrayList<>()));
			dataService.saveAUser(new AppUser("Luther", "Lex", "lex@dictator.com", "Me", new ArrayList<>()));

			//RolesToUser
			dataService.addARoleToAUser("Kal-El", "ROLE_SUPER_ADMIN");
			dataService.addARoleToAUser("Kal-El", "ROLE_ADMIN");
			dataService.addARoleToAUser("Kal-El", "ROLE_USER");

			dataService.addARoleToAUser("Wayne", "ROLE_ADMIN");
			dataService.addARoleToAUser("Wayne", "ROLE_USER");

			dataService.addARoleToAUser("Lex", "ROLE_USER");

		};


	}*/
}
