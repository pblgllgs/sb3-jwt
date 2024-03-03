package com.pblgllgs.sb3jwt.controller;

import com.pblgllgs.sb3jwt.dto.AuthRequestDto;
import com.pblgllgs.sb3jwt.dto.AuthResponseDto;
import com.pblgllgs.sb3jwt.dto.AuthStatus;
import com.pblgllgs.sb3jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());
        AuthResponseDto authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.LOGIN_SUCCESS);
        return new ResponseEntity<>(authResponseDto,HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDto> signUp(@RequestBody AuthRequestDto authRequestDto) {
        try {
            String jwtToken = authService.signUp(authRequestDto.name(), authRequestDto.username(), authRequestDto.password());
            AuthResponseDto authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.USER_CREATE_SUCCESSFULLY);
            return new ResponseEntity<>(authResponseDto,HttpStatus.CREATED);
        } catch (RuntimeException e){
            AuthResponseDto authResponseDto = new AuthResponseDto(null, AuthStatus.USER_CREATE_FAILED);
            return new ResponseEntity<>(authResponseDto,HttpStatus.CONFLICT);
        }
    }

}
