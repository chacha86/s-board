package com.korea.basic1.note;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> getNoteList() {
        return noteRepository.findAll();
    }

    public List<Note> getParentNoteList() {
        return noteRepository.findByParentId(null);
    }
    public Note getNoteById(Long id) {
        Optional<Note> optional = noteRepository.findById(id);
        if(optional.isPresent()) {
            return optional.get();
        }
        throw new IllegalArgumentException("해당 노트는 존재하지 않습니다.");
    }
    public Note getNoChildNote(Long id) {
        Note note = getNoteById(id);
        if(note.getChildren().isEmpty()) {
            return note;
        }

        return getNoteById(id + 1);
    }

    public Note saveDefaultNote() {
        Note note = Note.builder()
                .name("새노트")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .parent(null)
                .build();

        return noteRepository.save(note);
    }
    public Note getDefaultNotebook() {
        Note note = new Note();
        note.setName("새노트");
        note.setCreateDate(LocalDateTime.now());
        note.setUpdateDate(LocalDateTime.now());
        return noteRepository.save(note);
    }
    public Note saveGroupNotebook(Note parent) {
        Note child = getDefaultNotebook();
        child.setParent(parent);
        return noteRepository.save(child);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public Note save(Note note) {
        return noteRepository.save(note);
    }

    public void deleteChild(Note note) {
        List<Note> children = note.getChildren();
        for (Note child : children) {
            deleteChild(child);
            delete(child);
        }
    }

    public void updateNoteName(Long noteId, String noteName) {
        Note note = getNoteById(noteId);
        if (note == null) {
            new IllegalArgumentException("해당 노트는 존재하지 않습니다.");
        } else {
            note.setName(noteName);
            noteRepository.save(note); // save는 ID가 있으면 update, ID가 없으면 insert
        }
    }
}
