package com.ewd.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ewd.utils.JwtUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public JWTFilter(UserDetailsService userDetailsService, JwtUtils jwtUtils){
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Entered into doInternalFilter");
        log.info("REQUEST METHOD {}",request.getMethod());
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("Authorization Header is present");
            String token = authHeader.substring(7);
            if(token != null && !(jwtUtils.istokenExpired(token))) {
                log.info("Extracted token");
                UserDetails user = userDetailsService.loadUserByUsername(jwtUtils.extractName(token));
                UsernamePasswordAuthenticationToken authToken = new
                        UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("securityContextHolder set with user data");
            }

        }
        log.info("It's a free api no need to setup SecurityContextHolder");
        filterChain.doFilter(request, response);
    }

}