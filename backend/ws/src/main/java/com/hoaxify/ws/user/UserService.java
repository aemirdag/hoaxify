package com.hoaxify.ws.user;

import com.hoaxify.ws.configuration.CurrentUser;
import com.hoaxify.ws.email.EmailService;
import com.hoaxify.ws.file.FileService;
import com.hoaxify.ws.user.dto.UserUpdate;
import com.hoaxify.ws.user.exception.ActivationNotificationException;
import com.hoaxify.ws.user.exception.UserNotFoundException;
import com.hoaxify.ws.user.exception.UserTokenNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Autowired
    UserService(UserRepository userRepository,
                EmailService emailService,
                PasswordEncoder passwordEncoder,
                FileService fileService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.fileService = fileService;
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
        User inDB = userRepository
                .findByActivationToken(token)
                .orElseThrow(() -> new UserTokenNotFoundException(token));

        inDB.setActive(Boolean.TRUE);
        inDB.setActivationToken(null);
        userRepository.save(inDB);
    }

    public Page<User> getUsers(Pageable page, CurrentUser currentUser) {
        if (Objects.isNull(currentUser)) {
            return userRepository.findAll(page);
        }

        return userRepository.findByIdNot(currentUser.getId(), page);
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(long id, UserUpdate userUpdate) {
        User inDB = getUser(id);
        inDB.setUsername(userUpdate.username());

        if (Objects.nonNull(userUpdate.image()) && !userUpdate.image().isEmpty()) {
            fileService.deleteProfileImage(inDB.getImage());
            inDB.setImage(fileService.saveBase64StringAsFile(userUpdate.image()));
        }

        return userRepository.save(inDB);
    }

    public void deleteUser(long id) {
        User inDB = getUser(id);

        if (Objects.nonNull(inDB.getImage())) {
            fileService.deleteProfileImage(inDB.getImage());
        }

        userRepository.delete(inDB);
    }
}
