package com.hoaxify.ws.user.dto;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.validation.FileType;
import com.hoaxify.ws.user.validation.UniqueEmail;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdate(
        @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$",
                message = "{hoaxify.constraint.username.pattern}")
        String username,
        @FileType(types = {"jpeg", "png"})
        String image
) {

}
