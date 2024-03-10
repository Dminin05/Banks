package com.minin.banks.utils;

import com.minin.banks.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtProperties.getLifeTime().toMillis());

        return Jwts.builder()
                .addClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getAccessSecret(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromAccessToken(String token) {
        return getAllClaimsFromAccessToken(token).getSubject();
    }

    private Claims getAllClaimsFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getAccessSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getAccessSecret() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getAccessSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
