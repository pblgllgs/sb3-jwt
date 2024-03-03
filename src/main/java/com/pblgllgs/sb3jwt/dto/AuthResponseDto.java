package com.pblgllgs.sb3jwt.dto;

public record AuthResponseDto(
        String token,AuthStatus authStatus
) {
}
