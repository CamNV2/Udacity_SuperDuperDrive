package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoteService {
    private final NoteMapper noteMapper;
    private final UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    public List<Note> getListByUser(String userName){
        return noteMapper.findNoteByUser(userName);
    }

    public int addNote(Note note){
        return noteMapper.insertNote(new Note(null,note.getNoteTitle(),note.getNoteDescription(),note.getUserId()));
    }

    public int deleteNote(int noteID){
        return noteMapper.deleteNote(noteID);
    }

    public int updateNote(Note note){
        Note noteUd = noteMapper.findNoteById(note.getNoteId());
        noteUd.setNoteDescription(note.getNoteDescription());
        noteUd.setNoteTitle(note.getNoteTitle());
        return noteMapper.updateNote(noteUd);
    }

    public Note findNoteById(int id){
        return noteMapper.findNoteById(id);
    }

    public Note findNoteExist(String noteTitle, String description){
        return noteMapper.findNoteExist(noteTitle, description);
    }

}
