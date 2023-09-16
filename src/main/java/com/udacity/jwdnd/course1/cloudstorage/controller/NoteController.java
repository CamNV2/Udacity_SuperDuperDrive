package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NoteController {
    private final NoteService noteService;
    private final UserService userServicel;

    public NoteController(NoteService noteService, UserService userServicel) {
        this.noteService = noteService;
        this.userServicel = userServicel;
    }

    @GetMapping("/delete-note/{id}")
    public String deleteNote(@PathVariable int id){
        noteService.deleteNote(id);
        return"redirect:/home";
    }
    @PostMapping("/save-note")
    public String saveNote(@ModelAttribute("noteForm") Note note,Model model, Authentication authentication){
        User user = new User();
        user = userServicel.getUser(authentication.getName());
        if(note.getNoteId() !=null ){
            noteService.updateNote(note);
        } else if(note.getNoteId() == null){
            note.setUserId(user.getUserId());
            noteService.addNote(note);
        }
        model.addAttribute("noteList", noteService.getListByUser(user.getUsername()));
        return"redirect:/home";
    }
}
