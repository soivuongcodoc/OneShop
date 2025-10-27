package com.oneshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.oneshop.entity.Role;
import com.oneshop.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.List;
@SpringBootApplication
public class OneshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneshopApplication.class, args);
	}
	 @Bean
	    CommandLineRunner seedRoles(RoleRepository roleRepo) {
	        return args -> {
	            List<String> roles = List.of("ROLE_USER", "ROLE_VENDOR", "ROLE_ADMIN");
	            for (String r : roles) {
	                roleRepo.findByName(r).orElseGet(() -> roleRepo.save(Role.builder().name(r).build()));
	            }
	            System.out.println("✅ Seeded roles successfully.");
	        };
	    }
}
