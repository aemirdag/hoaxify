package com.hoaxify.ws.user;

import com.hoaxify.ws.auth.token.Token;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private boolean active = false;
    private String activationToken;
    private String username;
    private String email;
    private String password;
    @Lob
    private String image;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Token> tokens;
}
