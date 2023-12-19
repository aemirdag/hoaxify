package com.hoaxify.ws.user;

import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.dto.UserDTO;
import com.hoaxify.ws.user.exception.ActivationNotificationException;
import com.hoaxify.ws.user.exception.UserNotFoundException;
import com.hoaxify.ws.user.exception.UserTokenNotFoundException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/v1/users")
    ResponseEntity<GenericMessage> createUser(@Valid @RequestBody UserCreate userDTO) {
        userService.save(userDTO.toUser());

        String message = Messages.getMessageForLocale("hoaxify.create.user.success.message");
        return ResponseEntity.ok(new GenericMessage(message));
    }

    @PatchMapping("/api/v1/users/{token}/active")
    ResponseEntity<GenericMessage> activateUser(@PathVariable String token) {
        userService.activateUser(token);

        String message = Messages.getMessageForLocale("hoaxify.create.user.activate");
        return ResponseEntity.ok(new GenericMessage(message));
    }

    @GetMapping("/api/v1/users")
    ResponseEntity<Page<UserDTO>> getUsers(Pageable page) {
        return ResponseEntity.ok(userService.getUsers(page).map(UserDTO::new));
    }

    @GetMapping("/api/v1/users/{id}")
    ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        User user = userService.getUser(id);
        UserDTO userDTO = userService.mapModel(user);

        return ResponseEntity.ok(userDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgNotValidException(MethodArgumentNotValidException e) {
        ApiError apiError = ApiError.getApiError()
                .setPath("/api/v1/users")
                .setMessage(Messages.getMessageForLocale("hoaxify.error.validation"))
                .setStatus(HttpStatus.BAD_REQUEST.value());

        for (var fieldError : e.getBindingResult().getFieldErrors()) {
            apiError.putError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ActivationNotificationException.class)
    ResponseEntity<ApiError> handleActivationNotificationException(ActivationNotificationException e, HttpServletRequest request) {
        ApiError apiError = ApiError.getApiError()
                .setPath(request.getRequestURI())
                .setMessage(e.getMessage())
                .setStatus(HttpStatus.BAD_GATEWAY.value());

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(apiError);
    }

    @ExceptionHandler(UserTokenNotFoundException.class)
    ResponseEntity<ApiError> handleUserTokenNotFoundException(UserTokenNotFoundException e, HttpServletRequest request) {
        ApiError apiError = ApiError.getApiError()
                .setPath(request.getRequestURI())
                .setMessage(e.getMessage())
                .setStatus(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        ApiError apiError = ApiError.getApiError()
                .setPath(request.getRequestURI())
                .setMessage(e.getMessage())
                .setStatus(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
