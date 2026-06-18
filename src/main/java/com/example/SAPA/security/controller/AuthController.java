package com.example.SAPA.security.controller;

import com.example.SAPA.DTOs.Request.RegisterRequest;
import com.example.SAPA.security.DTO.AuthRequest;
import com.example.SAPA.security.DTO.AuthResponse;
import com.example.SAPA.security.DTO.RefreshTokenRequest;
import com.example.SAPA.security.entities.CredentialEntity;
import com.example.SAPA.security.repositories.CredentialRepository;
import com.example.SAPA.security.service.AuthService;
import com.example.SAPA.security.service.JWTService;
import com.example.SAPA.security.service.TokenBlacklistService;
import com.example.SAPA.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación, gestión de tokens y sesiones de usuario")
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;
    private final CredentialRepository credentialRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserService userService;

    @Operation(
            summary = "Autenticar usuario",
            description = "Autentica a un usuario utilizando correo electrónico y contraseña. Devuelve un token de acceso JWT y un token de refresco (refresh token)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticado exitosamente",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Credenciales inválidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        UserDetails user = authService.authenticate(authRequest);

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        CredentialEntity credential = credentialRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Credencial no encontrada."));

        credential.setRefreshToken(refreshToken);
        credentialRepository.save(credential);

        return ResponseEntity.ok(new AuthResponse(token, refreshToken));
    }

    @Operation(
            summary = "Renovar token de acceso",
            description = "Renueva el token de acceso utilizando un token de refresco válido. Devuelve un nuevo token de acceso y un nuevo token de refresco."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado exitosamente",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token de refresco inválido o expirado", content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cerrar sesión de usuario",
            description = "Cierra la sesión del usuario revocando el token de acceso actual. Añade el token a la lista negra."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesión cerrada exitosamente.", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "No se proporcionó un token válido.", content = @Content)
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest) {
        String auth = httpServletRequest.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);

            long tiempoExpiracion = jwtService.extractExpiration(token).getTime();
            tokenBlacklistService.addToBlacklist(token, tiempoExpiracion);

            return ResponseEntity.ok("Sesión cerrada exitosamente.");
        }
        return ResponseEntity.badRequest().body("No se proporcionó un token válido.");
    }

    @Operation(
            summary = "Olvidé mi contraseña",
            description = "Hace que el sistema envíe un email con un token de reinicio de contraseña utilizado en /reset-password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se envió el enlace de restablecimiento al correo.",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "No existe ningún usuario registrado con ese correo electrónico", content = @Content),
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Parameter(description = "Correo electrónico del usuario", required = true) @RequestParam String email) {
        try {
            authService.generateResetPasswordToken(email);
            return ResponseEntity.ok("Se envió el enlace de restablecimiento al correo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Reiniciar contraseña",
            description = "Mediante un token enviado por email desde el endpoint /forgot-password, permite cambiar la contraseña de la cuenta."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "El token de restablecimiento es inválido o mal formado.", content = @Content),
            @ApiResponse(responseCode = "410", description = "El token de restablecimiento es válido, pero ya caducó.", content = @Content)
    })
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Token recibido por correo electrónico", required = true) @RequestParam String token,
            @Parameter(description = "Nueva contraseña para la cuenta", required = true) @RequestParam String newPassword) {

        try {
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("El token de restablecimiento es inválido.");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.GONE).body("El token de restablecimiento es válido, pero ya caducó.");
        }
    }

    @Operation(
            summary = "Crear usuario",
            description = "Registra un nuevo usuario en el sistema. Devuelve las credenciales de acceso JWT inmediatas tras el registro."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "No se registró el usuario - El correo ya existe o los datos son inválidos", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse authResponse = userService.registerUser(request);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
