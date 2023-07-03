package com.rowi.docrecognizerapi.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KeycloakTestController {

    @GetMapping("/me")
    public Authentication whoAmI() {
        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println(context.getAuthentication());
        return context.getAuthentication();
    }

}

