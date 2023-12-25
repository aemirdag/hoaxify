package com.hoaxify.ws.user;

import com.hoaxify.ws.auth.token.TokenService;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.dto.UserDTO;
import com.hoaxify.ws.user.dto.UserUpdate;
import com.hoaxify.ws.user.exception.AuthorizationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/api/v1/users")
    public ResponseEntity<GenericMessage> createUser(@Valid @RequestBody UserCreate userDTO) {
        userService.save(userDTO.toUser());

        String message = Messages.getMessageForLocale("hoaxify.create.user.success.message");
        return ResponseEntity.ok(new GenericMessage(message));
    }

    @PatchMapping("/api/v1/users/{token}/active")
    public ResponseEntity<GenericMessage> activateUser(@PathVariable String token) {
        userService.activateUser(token);

        String message = Messages.getMessageForLocale("hoaxify.create.user.activate");
        return ResponseEntity.ok(new GenericMessage(message));
    }

    @GetMapping("/api/v1/users")
    public ResponseEntity<Page<UserDTO>> getUsers(Pageable page,
                                                  @RequestHeader(value = "Authorization", required = false)
                                                  String authorizationHeader) {
        User loggedInUser = tokenService.verifyToken(authorizationHeader);
        return ResponseEntity.ok(userService.getUsers(page, loggedInUser).map(UserDTO::new));
    }

    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        User user = userService.getUser(id);
        UserDTO userDTO = userService.mapModel(user);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/api/v1/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @Valid @RequestBody UserUpdate userUpdate,
                                              @RequestHeader(value = "Authorization", required = false)
                                              String authorizationHeader) {
        User loggedInUser = tokenService.verifyToken(authorizationHeader);
        if (Objects.isNull(loggedInUser) || loggedInUser.getId() != id) {
            throw new AuthorizationException();
        }

        User user = userService.updateUser(id, userUpdate);
        UserDTO userDTO = userService.mapModel(user);

        return ResponseEntity.ok(userDTO);
    }
}
