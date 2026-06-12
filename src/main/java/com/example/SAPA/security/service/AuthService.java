package com.example.SAPA.security.service;

import com.example.SAPA.security.DTO.AuthRequest;
import com.example.SAPA.security.DTO.AuthResponse;
import com.example.SAPA.security.entities.CredentialEntity;
import com.example.SAPA.security.repositories.CredentialRepository;
import com.example.SAPA.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final CredentialRepository credentialRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthService(CredentialRepository credentialRepository, AuthenticationManager authenticationManager,
                       JWTService jwtService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.credentialRepository = credentialRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public UserDetails authenticate(AuthRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );

        return credentialRepository.findByEmail(input.email()).orElseThrow();
    }

    @Transactional
    public AuthResponse refreshAccessToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);

        CredentialEntity user =
                credentialRepository.findByEmail(username)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token no coincide");
        }

        if (!jwtService.validateRefreshToken(refreshToken, user)) {
            throw new IllegalArgumentException("Refresh token expirado o inválido");
        }

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        System.out.println(newAccessToken);
        user.setRefreshToken(newRefreshToken);
        credentialRepository.save(user);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public void generateResetPasswordToken(String email) {
        CredentialEntity credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No existe ningún usuario registrado con ese correo electrónico."));

        String token = UUID.randomUUID().toString();

        credential.setResetPasswordToken(token);

        credential.setResetPasswordTokenExpiration(LocalDateTime.now().plusMinutes(15));

        credentialRepository.save(credential);

        emailService.sendResetPasswordEmail(credential.getEmail(), token);
    }


    public void resetPassword(String token, String newPassword) {

        CredentialEntity credential = credentialRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("El token de restablecimiento es inválido."));

        if (credential.getResetPasswordTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El enlace ha expirado. Por favor, solicite uno nuevo.");
        }

        credential.setPassword(passwordEncoder.encode(newPassword));

        credential.setResetPasswordToken(null);
        credential.setResetPasswordTokenExpiration(null);

        credentialRepository.save(credential);
    }
}
