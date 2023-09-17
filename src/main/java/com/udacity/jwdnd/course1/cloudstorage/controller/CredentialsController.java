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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CredentialsController {

    private final CredentialsService credentialsService;

    private final UserService userService;

    public CredentialsController(CredentialsService credentialsService, UserService userService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
    }

    @GetMapping("/delete-credentials/{id}")
    public String deleteCredentials(@PathVariable int id, RedirectAttributes redirectAttributes){
        int cnt = credentialsService.deleteById(id);
        if (cnt > 0) {
            String successMsg = "Credential deleted successfully!";
            redirectAttributes.addFlashAttribute("messageSuccess", successMsg);
            return"redirect:/home";
        }
        String errorMsg = "Credential deleted failure!";
        redirectAttributes.addFlashAttribute("messageError", errorMsg);
        return"redirect:/home";
    }

    @PostMapping("/save-credentials")
    public String saveCredentials(@ModelAttribute("credentialsFrom") Credentials credentials, Model model, Authentication authentication, RedirectAttributes redirectAttributes){
        User user = userService.getUser(authentication.getName());
        String successMsg = null;
        boolean isExistUser = credentialsService.checkExistUserName(credentials.getUsername());
        if (credentials.getCredentialId() != null ){

            if (isExistUser){
                String errorMsg = "User already available!";
                redirectAttributes.addFlashAttribute("messageError", errorMsg);
                return"redirect:/home";
            }
            int cntUpt = credentialsService.updateCredentials(credentials);
            if (cntUpt > 0) {
                successMsg = "Credential update successfully!";
            } else {
                String errorMsg ="Credential updated failure!";
                redirectAttributes.addFlashAttribute("messageError", errorMsg);
                return"redirect:/home";
            }
        } else if(credentials.getCredentialId() == null){
            credentials.setUserId(user.getUserId());

            if (isExistUser) {
                String errorMsg = "User already available!";
                redirectAttributes.addFlashAttribute("messageError", errorMsg);
                return"redirect:/home";
            }
            int cnt = credentialsService.addCredentials(credentials);
            if (cnt > 0) {
                successMsg = "Credential created successfully!";
            } else {
                String errorMsg = "Credential created failure!";
                redirectAttributes.addFlashAttribute("messageError", errorMsg);
                return"redirect:/home";
            }
        }
        redirectAttributes.addFlashAttribute("messageSuccess", successMsg);
        model.addAttribute("credentialsList", credentialsService.getListAll());
        return"redirect:/home";
    }

}