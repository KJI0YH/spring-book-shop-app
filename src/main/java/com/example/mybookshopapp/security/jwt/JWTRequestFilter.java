package com.example.mybookshopapp.security.jwt;

import com.example.mybookshopapp.security.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;
    private final JwtBlackListService jwtBlackListService;

    public JWTRequestFilter(CustomUserDetailsService customUserDetailsService, JWTUtil jwtUtil, JwtBlackListService jwtBlackListService) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtBlackListService = jwtBlackListService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, BadCredentialsException {
        Cookie[] cookies = request.getCookies();
        Cookie tokenCookie = getTokenCookie(cookies);
        String token = tokenCookie == null ? null : tokenCookie.getValue();

        try {
            if (jwtBlackListService.isInBlackList(token))
                throw new BadCredentialsException("Token is blacklisted");

            String username = token == null ? null : jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (BadCredentialsException | ExpiredJwtException exception) {
            if (tokenCookie != null) {
                tokenCookie.setMaxAge(0);
                tokenCookie.setPath("/");
                response.addCookie(tokenCookie);
            }
            response.sendRedirect("/signin");
        }
    }

    private Cookie getTokenCookie(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie;
            }
        }
        return null;
    }
}
