package com.example.testProject.services;

import com.example.testProject.models.UserDataRequest;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

// Этот класс генерит и валидирует токен. Данный код я просто скопировал с мануала библиотеки io.jsonwebtoken, ничего не писал с нуля.
@Service
public class TokenGeneratorUtility {
    private String secret;
    private int jwtExpirationInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public String generateToken(UserDataRequest data) {
        return Jwts.builder().setSubject(data.getName()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public boolean validateToken(String token) throws Exception {
        try {
            System.out.println("TOKEN:");
            System.out.println(token);
            // данный код проверяет токен на валидацию
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
            // все остальное снизу тригеррит исключения
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new Exception("Некорректные данные", ex);
        } catch (ExpiredJwtException ex) {
            throw new Exception("Токен просрочен", ex);
        }
    }
}
