package com.hoaxify.ws.user;

import com.hoaxify.ws.email.EmailService;
import com.hoaxify.ws.user.exception.ActivationNotificationException;
import com.hoaxify.ws.user.exception.UserTokenNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    final private UserRepository userRepository;
    final private EmailService emailService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional(rollbackOn = MailException.class)
    public void save(User user) {
        try {
            user.setActivationToken(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);

            emailService.sendActivationEmail(user.getEmail(), user.getActivationToken());
        } catch (MailException e) {
            throw new ActivationNotificationException();
        }
    }

    @Transactional(rollbackOn = UserTokenNotFoundException.class)
    public void activateUser(String token) {
        User inDB = userRepository.findByActivationToken(token);

        if (inDB == null) {
            throw new UserTokenNotFoundException(token);
        }

        inDB.setActive(true);
        inDB.setActivationToken(null);
        userRepository.save(inDB);
    }
}
