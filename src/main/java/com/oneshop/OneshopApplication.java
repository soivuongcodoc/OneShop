package com.oneshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.oneshop.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OneshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneshopApplication.class, args);
	}
	 @Bean
	    CommandLineRunner seedRoles(RoleRepository roleRepo) {
	        return args -> {
	            // Show all roles in database
	            System.out.println("📋 All roles in database:");
	            roleRepo.findAll().forEach(role -> 
	                System.out.println("   - " + role.getName() + " (ID: " + role.getId() + ")")
	            );
	        };
	    }
}
