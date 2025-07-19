package com.zerobase.socialgroupplatform.security;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secretKey;

  private final long EXPIRATION_TIME = 1000L * 60 * 60 * 24;

  // 토큰 생성
  public String createToken(String userId, String role) {
    Claims claims = Jwts.claims().setSubject(userId);
    claims.put("role", role);

    Date now = new Date();
    Date expire = new Date(now.getTime() + EXPIRATION_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expire)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  // 토큰에서 이메일 추출
  public String getEmail(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  // 유효성 검사
  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}

