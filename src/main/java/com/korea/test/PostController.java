package com.korea.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @RequestMapping("/test")
    @ResponseBody public String test() {
        return "test";
    }

    @RequestMapping("/")
    public String main(Model model,@RequestParam(value = "noteId",defaultValue = "1")Long noteId,@RequestParam(value = "id",defaultValue = "1")Long id) {
        //1. DB에서 데이터 꺼내오기
        List<Post> postList = postRepository.findAll();
        List<Note> noteList = noteRepository.findAll();
        if (noteList.isEmpty()){
            noteService.noteWrite();
            return "redirect:/";
        }
        if (noteList.get(0).getPosts().isEmpty()){
            write(noteId,id);
        }
        List<Post> postListForNote = noteList.get(0).getPosts();
        //2. 꺼내온 데이터를 템플릿으로 보내기
        model.addAttribute("targetNote",noteList.get(0));
        model.addAttribute("noteList",noteList);
        model.addAttribute("postList",postListForNote);
        model.addAttribute("targetPost", postList.get(0));

        return "main";
    }

    @PostMapping("/write")
    public String write(Long noteId,Long id) {
        Post post = new Post();
        Note note = noteRepository.findById(noteId).get();
        post.setTitle("new title..");
        post.setContent("");
        post.setCreateDate(LocalDateTime.now());
        post.setNote(note);

        postRepository.save(post);

        return "redirect:/detail/"+noteId+"/"+ id;
    }

    @GetMapping("/detail/{noteId}/{id}")
    public String detail(Model model, @PathVariable Long noteId,@PathVariable Long id) {
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
    @PostMapping("/update")
    public String update(Long id, String title, String content,Long noteId) {
        Post post = postRepository.findById(id).get();
        post.setTitle(title);
        post.setContent(content);
        if (post.getTitle().equals("")){
            post.setTitle("제목없음");
        }

        postRepository.save(post);
        return "redirect:/detail/"+noteId+"/"+ id;
    }
    @PostMapping("/delete")
    public String delete(Long id){
        Post post = postRepository.findById(id).get();
        Note note = post.getNote();
        postRepository.delete(post);
        if (note.getPosts().isEmpty()) {

        }
        return "redirect:/";
    }
}
