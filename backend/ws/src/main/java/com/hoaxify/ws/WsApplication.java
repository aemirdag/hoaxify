package com.hoaxify.ws;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class WsApplication {
	public static void main(String[] args) {
		SpringApplication.run
				(WsApplication.class, args);
	}

	@Bean
	@Profile("dev")
	protected CommandLineRunner userCreator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {
			var userInDB = userRepository.findByEmail("userTemp1@mail.com");
			if (userInDB.isPresent()) {
				return;
			}

			for (int i = 1; i <= 25; i++) {
				User user = new User();
				user.setUsername("userTemp" + i);
				user.setEmail("userTemp" + i + "@mail.com");
				user.setPassword(passwordEncoder.encode("P4ssword@" + i));
				user.setActive(Boolean.TRUE);

				userRepository.save(user);
			}

			User user = new User();
			user.setUsername("userTemp" + 26);
			user.setEmail("userTemp" + 26 + "@mail.com");
			user.setPassword(passwordEncoder.encode("P4ssword@" + 26));
			user.setActive(Boolean.FALSE);

			userRepository.save(user);
		};
	}
}
