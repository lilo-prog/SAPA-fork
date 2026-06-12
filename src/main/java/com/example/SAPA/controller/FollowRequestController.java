package com.example.SAPA.controller;

import com.example.SAPA.Models.FollowRequestEntity;
import com.example.SAPA.service.FollowRequestService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/follow-requests")
public class FollowRequestController {
    //Atributos.
    private final FollowRequestService followRequestService;

    //Constructor.
    public FollowRequestController(FollowRequestService followRequestService) {
        this.followRequestService = followRequestService;
    }

    //Metodos.
    @PostMapping
    public FollowRequestEntity create(@RequestBody FollowRequestEntity request){
        return followRequestService.create(request);
    }

    @GetMapping
    public List<FollowRequestEntity> getAll(){
        return followRequestService.getAll();
    }

    @PutMapping("/{id}/approve")
    public FollowRequestEntity approve(@PathVariable Long id){
        return followRequestService.approve(id);
    }

    @PutMapping("/{id}/reject")
    public FollowRequestEntity reject(@PathVariable Long id){
        return followRequestService.reject(id);
    }

    @PutMapping("/{id}/dissolve")
    public FollowRequestEntity dissolve(@PathVariable Long id){
        return followRequestService.dissolve(id);
    }
}
