package com.pblgllgs.sb3jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/secret")
public class SecretController {

    @GetMapping
    public ResponseEntity<String> getSecret(){
        return new ResponseEntity<>(UUID.randomUUID().toString(), HttpStatus.OK);
    }
}
