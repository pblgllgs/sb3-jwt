package com.pblgllgs.sb3jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Document(collection = "app-users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser {
    private String id;
    private String name;
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;
}
