package com.hoaxify.ws.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private String image;
}
