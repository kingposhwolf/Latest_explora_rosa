package com.example.demo.Services.JWTService;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JWTService {

   @Override
   public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 24))
        .signWith(getSiginKey(), SignatureAlgorithm.HS256)
        .compact();
   }

   @Override
   public String generateRefreshToken(Map<String,Object> extraClaims ,UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+ 604800000))
        .signWith(getSiginKey(), SignatureAlgorithm.HS256)
        .compact();
   }

   private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
      final Claims claims = extractAllClaims(token);
      return claimsResolvers.apply(claims);
   }

   private Claims extractAllClaims(String token) {
      return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
   }

   public String extractUserName(String token){
      return extractClaim(token, Claims::getSubject);
   }

   private Key getSiginKey(){
    byte[] key = Decoders.BASE64.decode("R29kIGlzIEdyZWF0R29kIGlzIEdyZWF0R29kIGlzIEdyZWF0");
    return Keys.hmacShaKeyFor(key);
    
   }

   @Override
   public boolean isTokenValid(String token, UserDetails userDetails) {
      final String username = extractUserName(token);

      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }

   @Override
   public boolean isTokenExpired(String token) {
      return extractClaim(token, Claims::getExpiration).before(new Date());
   }
}
