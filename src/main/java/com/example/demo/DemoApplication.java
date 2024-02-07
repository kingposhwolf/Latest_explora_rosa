package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.UserRepository;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	public void run(String... Args){
		User adminAccount = userRepository.findByRole(Role.ADMIN);

		if(null == adminAccount){
			User user = new User();

			user.setEmail("admin@mail.com");
			user.setFirstName("admin");
			user.setSecondName("admin");
			user.setUsername("admin");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("password"));

			userRepository.save(user);
		}
	}

}
