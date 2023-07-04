package com.rowi.docrecognizerapi.controller;


import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class KeycloakTestController {

    @GetMapping("/me")
    public String getCurrentUser(
            KeycloakAuthenticationToken token
    ) {
        return token.getName();
    }
}
