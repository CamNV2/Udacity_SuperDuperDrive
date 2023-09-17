package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    private final UserService userService;
    private final NoteService noteService;
    private final FileService fileService;
    private final CredentialsService credentialsService;

    public HomeController(UserService userService, NoteService noteService, FileService fileService, CredentialsService credentialsService) {
        this.userService = userService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/home")
    public String getHomePage(Model model, Authentication authentication){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("fileList",fileService.getListFileByUserId(user.getUserId()));
        model.addAttribute("credentialsList", credentialsService.getListAll());
        model.addAttribute("user", userService.getUser(authentication.getName()));
        model.addAttribute("noteList", noteService.getListByUser(authentication.getName()));
        return "home";
    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }
}
