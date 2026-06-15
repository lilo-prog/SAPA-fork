package com.example.SAPA.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Restablecer contraseña - SAPA");
        message.setText("Hacé click en el siguiente enlace para restablecer tu contraseña:\n\n"
                + "https://sapa.com/reset-password?token=" + token
                + "\n\nEste enlace expira en 15 minutos.");
        mailSender.send(message);
    }
}
