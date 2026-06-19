package com.example.SAPA.service;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailService {

    private final MailtrapClient mailtrapClient;

    public EmailService(MailtrapClient mailtrapClient) {
        this.mailtrapClient = mailtrapClient;
    }

    public void sendResetPasswordEmail(String toEmail, String token) {

        String url = "https://sapa.com" + token;

        String cuerpoMensaje = "Hacé click en el siguiente enlace para restablecer tu contraseña:\n\n"
                + url + "\n\nEste enlace expira en 15 minutos.";

        MailtrapMail mail = MailtrapMail.builder()
                .from(new Address("no-reply@sapa.com", "SAPA Sistema"))
                .to(List.of(new Address(toEmail)))
                .subject("Restablecer contraseña - SAPA")
                .text(cuerpoMensaje)
                .build();

        try {
            mailtrapClient.send(mail);
        } catch (Exception e) {
            System.err.println("Error al enviar con el SDK de Mailtrap: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo de recuperación", e);
        }
    }
}
