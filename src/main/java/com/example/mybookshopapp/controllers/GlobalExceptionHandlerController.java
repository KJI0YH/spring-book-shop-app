package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.errors.UserAlreadyExistException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginError", e);
        return "redirect:/signin";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("authError", e);
        return "redirect:/signin";
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserAlreadyExistException(UserAlreadyExistException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("regError", e);
        return "redirect:/signup";
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("authError", e);
        return "redirect:/signin";
    }
}
