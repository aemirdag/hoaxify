package com.hoaxify.ws.configuration;

import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserService;
import com.hoaxify.ws.user.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AppUserDetailsService(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User inDB = userService.findByEmail(email).orElseThrow(() -> {
            return new UserNotFoundException(
                    Messages.getMessageForLocale("hoaxify.user.email.not.found", email));
        });

        return modelMapper.map(inDB, CurrentUser.class);
    }
}
