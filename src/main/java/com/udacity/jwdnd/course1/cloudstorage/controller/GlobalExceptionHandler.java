package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
        String uploadError = "The file is too large. Please upload a file under 5MB!";
        redirectAttributes.addFlashAttribute("messageError", uploadError);
        return"redirect:/home";
    }
}
