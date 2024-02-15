package com.hoaxify.ws.auth.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoaxify.ws.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Token {
    @Id
    private String token;
    @Transient
    private String prefix = "Bearer";
    @JsonIgnore
    @ManyToOne
    private User user;

    public Token(String token) {
        this.token = token;
    }

    public Token(String token, String prefix) {
        this.token = token;
        this.prefix = prefix;
    }

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
