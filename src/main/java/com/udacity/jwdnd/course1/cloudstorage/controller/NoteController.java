package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/delete-note/{id}")
    public String deleteNote(@PathVariable int id, RedirectAttributes redirectAttributes){
        int cnt = noteService.deleteNote(id);
        if (cnt > 0) {
            String successMsg = "Note deleted successfully";
            redirectAttributes.addFlashAttribute("messageSuccess", successMsg);
            return"redirect:/home";
        }
        String errorMsg = "Note deleted failure.";
        redirectAttributes.addFlashAttribute("messageError", errorMsg);
        return"redirect:/home";
    }
    @PostMapping("/save-note")
    public String saveNote(@ModelAttribute("noteForm") Note note,Model model, Authentication authentication, RedirectAttributes redirectAttributes){
        User user = userService.getUser(authentication.getName());
        String successMsg = null;
        String errorMsg = null;
        if (note.getNoteId() != null ) {
            int cnt = noteService.updateNote(note);
            if(cnt > 0) {
                successMsg = "Note updated successfully.";
            } else {
                errorMsg ="Note updated failure";
                redirectAttributes.addFlashAttribute("messageError", errorMsg);
                return"redirect:/home";
            }
        } else if(note.getNoteId() == null) {
            Note noteAvailable = noteService.findNoteExist(note.getNoteTitle(),note.getNoteDescription());
            if(noteAvailable != null) {
                errorMsg = "Note already available.";
                redirectAttributes.addFlashAttribute("messageError", errorMsg);
                return"redirect:/home";
            }
            note.setUserId(user.getUserId());
            int cnt = noteService.addNote(note);
            if(cnt > 0) {
                successMsg = "Note created successfully";
            } else {
                errorMsg ="Note created failure";
                redirectAttributes.addFlashAttribute("messageError", errorMsg);
                return"redirect:/home";
            }
        }

        redirectAttributes.addFlashAttribute("messageSuccess", successMsg);
        model.addAttribute("noteList", noteService.getListByUser(user.getUsername()));
        return"redirect:/home";
    }
}
