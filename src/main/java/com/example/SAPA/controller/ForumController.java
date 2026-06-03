package com.example.SAPA.controller;
import com.example.SAPA.Models.Forum.ForumEntity;
import com.example.SAPA.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("forums")
public class ForumController {
    @Autowired
    private ForumService forumService;

    @PostMapping
    public ForumEntity createForum(@RequestBody ForumEntity forum){
        return forumService.create(forum);
    }

    @GetMapping("/{id}")
    public  ForumEntity updateForum(@PathVariable Long id, @RequestBody ForumEntity forum){
        return forumService.update(id, forum);
    }

    @DeleteMapping("/{id}")
    public void deleteForum(@PathVariable Long id){
        forumService.delete(id);
    }

    @GetMapping("/search")
    public List<ForumEntity> search(@RequestParam String forumName){
        return forumService.search(forumName);
    }
}
