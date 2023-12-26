package com.hoaxify.ws.user;

import com.hoaxify.ws.auth.token.TokenService;
import com.hoaxify.ws.configuration.CurrentUser;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.dto.UserDTO;
import com.hoaxify.ws.user.dto.UserUpdate;
import com.hoaxify.ws.user.exception.AuthorizationException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
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
                                                  @AuthenticationPrincipal CurrentUser currentUser) {
        Page<UserDTO> userDTOs = userService.getUsers(page, currentUser).map(UserDTO::new);

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        User user = userService.getUser(id);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/api/v1/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id,
                                              @Valid @RequestBody UserUpdate userUpdate,
                                              @AuthenticationPrincipal CurrentUser currentUser) {
        if (Objects.isNull(currentUser) || currentUser.getId() != id) {
            throw new AuthorizationException();
        }

        User user = userService.updateUser(id, userUpdate);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.ok(userDTO);
    }
}
