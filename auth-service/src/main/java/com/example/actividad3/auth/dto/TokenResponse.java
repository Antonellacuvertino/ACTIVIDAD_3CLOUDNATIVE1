package com.example.actividad3.auth.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) { }
