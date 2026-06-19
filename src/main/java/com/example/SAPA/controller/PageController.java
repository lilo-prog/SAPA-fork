package com.example.SAPA.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping({"/index"})
    public String index() {
        return "redirect:/index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro-paciente")
    public String registroPaciente() {
        return "registro-paciente";
    }

    @GetMapping("/registro-medico")
    public String registroMedico() {
        return "registro-medico";
    }

    @GetMapping("/perfil")
    public String profile(){return "perfil";}

}
