package com.pblgllgs.sb3jwt.service;

public interface AuthService {
    String login(String name, String password);

    String signUp(String name, String username, String password);
}