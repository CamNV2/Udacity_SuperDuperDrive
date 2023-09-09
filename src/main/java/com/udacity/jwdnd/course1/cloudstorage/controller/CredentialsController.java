package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialsController {

    private final CredentialsService credentialsService;

    private final UserService userService;

    public CredentialsController(CredentialsService credentialsService, UserService userService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
    }

    @GetMapping("/delete-Credentials/{id}")
    public String deleteCredentials(@PathVariable int id){
        credentialsService.deleteById(id);
        return"redirect:/home";
    }

    @PostMapping("saveCredentials")
    public String saveCredentials(@ModelAttribute("credentialsFrom") Credentials credentials, Model model, Authentication authentication){
        User user = userService.getUser(authentication.getName());
        if(credentials.getCredentialId() != null ){
            credentialsService.updateCredentials(credentials);
        } else if(credentials.getCredentialId() == null){
            credentials.setUserId(user.getUserId());
            credentialsService.addCredentials(credentials);
        }
        model.addAttribute("credentialsList", credentialsService.getListCredentialsByUserName(user.getUsername()));
        return"redirect:/home";
    }
}