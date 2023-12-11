package com.hoaxify.ws.user;

import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.exception.ActivationNotificationException;
import com.hoaxify.ws.user.exception.UserTokenNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    final private UserService userService;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgNotValidException(MethodArgumentNotValidException e) {
        String message = Messages.getMessageForLocale("hoaxify.error.validation");

        ApiError apiError = ApiError.getApiError()
                .setPath("/api/v1/users")
                .setMessage(message)
                .setStatus(HttpStatus.BAD_REQUEST.value());

        for (var fieldError : e.getBindingResult().getFieldErrors()) {
            apiError.putError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ActivationNotificationException.class)
    ResponseEntity<ApiError> handleActivationNotificationException(ActivationNotificationException e) {
        String message = e.getMessage();

        ApiError apiError = ApiError.getApiError()
                .setPath("/api/v1/users")
                .setMessage(message)
                .setStatus(HttpStatus.BAD_GATEWAY.value());

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(apiError);
    }

    @ExceptionHandler(UserTokenNotFoundException.class)
    ResponseEntity<ApiError> handleUserTokenNotFoundException(UserTokenNotFoundException e) {
        String message = Messages.getMessageForLocale("hoaxify.create.user.token.find.failure");

        ApiError apiError = ApiError.getApiError()
                .setPath("/api/v1/users/" + e.getToken() + "active")
                .setMessage(message)
                .setStatus(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
