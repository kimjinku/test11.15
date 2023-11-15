package com.korea.test;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class NoteController {
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;
    @PostMapping("/noteWrite")
    public String noteWrite(Long noteId,Long id) {
        Note note = new Note();
        List<Post> postList = new ArrayList<>();
        note.setPosts(postList);
        note.setTitle("새 노트");
        noteRepository.save(note);
        postService.write(noteRepository.findMaxNoteId());
        return "redirect:/detail/"+noteId+"/"+ id;
    }
    @GetMapping("/noteDetail/{noteId}")
    public String noteDetail(Model model, @PathVariable Long noteId, @PathVariable Long id) {
        Post post = postRepository.findById(id).get();
        Note note = noteRepository.findById(noteId).get();
        List<Note> noteList = noteRepository.findAll();
        List<Post> postListForNote = note.getPosts();
        model.addAttribute("targetPost", post);
        model.addAttribute("postList", postListForNote);
        model.addAttribute("targetNote",note);
        model.addAttribute("noteList",noteList);

        return "main";
    }
}
