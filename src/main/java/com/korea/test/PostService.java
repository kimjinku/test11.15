package com.korea.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PostService {
    final PostRepository postRepository;
    final NoteRepository noteRepository;
    public void write(Long noteId){
        Post post = new Post();
        Note note = noteRepository.findById(noteId).get();
        post.setTitle("new title..");
        post.setContent("");
        post.setCreateDate(LocalDateTime.now());
        post.setNote(note);

        postRepository.save(post);
    }

}
