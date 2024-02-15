package com.hoaxify.ws.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "hoaxify")
public class HoaxifyProperties {
    @Setter
    private Email email;
    @Setter
    private Client client;
    private final Storage storage = new Storage();
    @Setter
    private String tokenType;

    public static record Email(
            String username,
            String password,
            String host,
            int port,
            String from
    ){}

    public static record Client(
            String host
    ){}

    @Setter
    @Getter
    public static class Storage {
        String root = "uploads";
        String profile = "profile";
    }
}
