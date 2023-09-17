package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.io.IOException;
import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/add-file")
    public String addFile(@RequestParam("fileUpload") MultipartFile fileUpload,Authentication authentication, Model model, RedirectAttributes redirectAttributes) throws IOException {
        String uploadError = null;
        String successMsg = null;
        int cnt = 0;
        User user = userService.getUser(authentication.getName());

        if (fileUpload.isEmpty()) {
            uploadError = "Please select a non-empty file!";
            redirectAttributes.addFlashAttribute("messageError", uploadError);
            return"redirect:/home";
        }
        if (!fileService.isExistFile(fileUpload.getOriginalFilename(), user.getUserId())) {
            uploadError = "The file already exists!";
            redirectAttributes.addFlashAttribute("messageError", uploadError);
            return"redirect:/home";
        }

        try {
            cnt = fileService.addFile(fileUpload, user.getUserId());
        } catch (MaxUploadSizeExceededException e) {
            throw e; // throw exception to accept GlobalExceptionHandler handle
        }

        if(cnt > 0) {
            successMsg = "File uploaded successfully!";
            redirectAttributes.addFlashAttribute("messageSuccess",successMsg);
            return "redirect:/home";
        }
        uploadError = "File uploaded failure!";
        model.addAttribute("messageError",uploadError);
        return "redirect:/home";
    }

    @GetMapping("delete-file/{id}")
    public String deleteFile(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        int cnt = fileService.deleteFile(id);
        if (cnt > 0) {
            String successMsg = "File deleted successfully!";
            redirectAttributes.addFlashAttribute("messageSuccess", successMsg);
            return"redirect:/home";
        }
        String errorMsg = "File deleted failure!";
        redirectAttributes.addFlashAttribute("messageError", errorMsg);
        return"redirect:/home";
    }

    @GetMapping("download-file/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer id){
        File file = fileService.getFileById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file.getFileName()+"\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

}
