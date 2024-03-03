package com.pblgllgs.sb3jwt.service.impl;


import com.pblgllgs.sb3jwt.entity.AppUser;
import com.pblgllgs.sb3jwt.repository.AppUserRepository;
import com.pblgllgs.sb3jwt.service.AuthService;
import com.pblgllgs.sb3jwt.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;

    @Override
    public String login(String name, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(name,password);
        Authentication authenticate = authenticationManager.authenticate(authToken);

        return JwtUtils.generateToken(((UserDetails) (authenticate.getPrincipal())).getUsername());
    }

    @Override
    public String signUp(String name, String username, String password) {
        if (appUserRepository.existsByUsername(username)) {
            throw new RuntimeException("USER_ALREADY_EXISTS");
        }

        var authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        var encodePassword = passwordEncoder.encode(password);
        var user = AppUser.builder()
                .name(name)
                .username(username)
                .password(encodePassword)
                .authorities(authorities)
                .build();
        appUserRepository.save(user);

        return JwtUtils.generateToken(username);
    }
}
