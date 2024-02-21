package com.hoaxify.ws.configuration;

import com.hoaxify.ws.auth.token.TokenService;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final HandlerExceptionResolver exceptionResolver;

    @Autowired
    public TokenFilter(TokenService tokenService,
                       @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.tokenService = tokenService;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tokenWithPrefix = getTokenWithPrefix(request);
        if (Objects.nonNull(tokenWithPrefix) && !tokenWithPrefix.isEmpty()) {
            User user = tokenService.verifyToken(tokenWithPrefix);

            if (Objects.nonNull(user)) {
                if (!user.isActive()) {
                    exceptionResolver.resolveException(request, response, null,
                            new DisabledException(Messages.getMessageForLocale("hoaxify.auth")));

                    return;
                }

                CurrentUser currentUser = new CurrentUser(user);

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(currentUser,
                        null, currentUser.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenWithPrefix(HttpServletRequest request) {
        String tokenWithPrefix = request.getHeader("Authorization");
        Cookie[] cookies = request.getCookies();

        if (Objects.isNull(cookies)) {
            return tokenWithPrefix;
        }

        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("hoax-token")) {
                continue;
            }

            if (Objects.isNull(cookie.getValue()) || cookie.getValue().isEmpty()) {
                continue;
            }

            return "AnyPrefix " + cookie.getValue();
        }

        return tokenWithPrefix;
    }
}
