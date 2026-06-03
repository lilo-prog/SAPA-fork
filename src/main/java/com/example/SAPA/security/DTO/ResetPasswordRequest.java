package com.example.SAPA.security.DTO;

public record ResetPasswordRequest(String token, String newPassword) {}
