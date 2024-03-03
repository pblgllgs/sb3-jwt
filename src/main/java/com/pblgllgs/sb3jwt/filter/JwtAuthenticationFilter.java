package com.pblgllgs.sb3jwt.filter;

import com.pblgllgs.sb3jwt.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> jwtTokenOptional = getTokenFromRequest(request);

        jwtTokenOptional.ifPresent(jwtToken -> {
            if (JwtUtils.validateToken(jwtToken)) {
                var usernameFromTokenOptional = JwtUtils.getUsernameFromToken(jwtToken);
                usernameFromTokenOptional.ifPresent(usernameFromToken -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);

                    //Create authentication token
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            null,
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //set authentication to security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        });
        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        //get token from header 'Authorization'
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        //validate no null and starts with 'Bearer '
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }
        return Optional.empty();
    }
}
