package com.pblgllgs.sb3jwt.service.impl;

import com.pblgllgs.sb3jwt.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        return new User(appUser.getUsername(), appUser.getPassword(), appUser.getAuthorities());
    }
}
