package com.example.SAPA.service;

import com.example.SAPA.Models.Forum.ForumEntity;
import com.example.SAPA.Repositories.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumService {

     @Autowired
    private ForumRepository forumRepository;

     public ForumEntity create(ForumEntity forum){
         return forumRepository.save(forum);
     }

     public List<ForumEntity> getAll(){
         return forumRepository.findAll();
     }

     public ForumEntity update(Long id, ForumEntity forum){
         ForumEntity existing = forumRepository.findById(id).orElseThrow(() -> new RuntimeException("Foro no encontrado."));

         existing.setTitle(forum.getTitle());
         existing.setDescription(forum.getDescription());

         return forumRepository.save(existing);
     }

     public void delete(Long id){
         forumRepository.deleteById(id);
     }

     public List<ForumEntity> search(String forumName){
         return forumRepository.findByForumName(forumName);
     }
}
