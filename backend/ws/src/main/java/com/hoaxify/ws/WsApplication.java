package com.hoaxify.ws;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WsApplication {

	public static void main(String[] args) {
		SpringApplication.run
				(WsApplication.class, args);
	}

	@Bean
	@Profile("dev")
	protected CommandLineRunner userCreator(UserRepository userRepository) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		return (args) -> {
			for (int i = 1; i <= 25; i++) {
				User user = new User();
				user.setUsername("userTemp" + i);
				user.setEmail("userTemp" + i + "@mail.com");
				user.setPassword(passwordEncoder.encode("P4ssword@" + i));
				user.setActive(Boolean.TRUE);

				userRepository.save(user);
			}
		};
	}
}
