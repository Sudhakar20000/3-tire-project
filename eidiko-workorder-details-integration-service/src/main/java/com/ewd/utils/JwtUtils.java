package com.ewd.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final String SIGNATUREKEY = "f3ef4f303d10f3dff46420489545aad6e5c3dc2435731b92be9ca975baa6b393";
    private static final Long EXPIRATION = 48*60*60*1000L;
    private static final Long REFRESHTOKEN_EXPIRATION = 12 * EXPIRATION;

    public String genrateToken(UserDetails userDetails, boolean isRefreshToken) {
        return genrateToken(new HashMap<>(),userDetails,isRefreshToken);
    }

    public String genrateToken(Map<String,Object> extraClaims, UserDetails userDetails,
                               boolean isRefreshToken) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setExpiration(new Date(System.currentTimeMillis()+getExpiration(isRefreshToken)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(userDetails.getUsername())
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Long getExpiration(boolean isRefreshToken){
        return isRefreshToken?REFRESHTOKEN_EXPIRATION:EXPIRATION;
    }

    private Key getSignKey() {
        byte [] key = Decoders.BASE64.decode(SIGNATUREKEY);
        return Keys.hmacShaKeyFor(key);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claim = extractAllClaims(token);
        return claimResolver.apply(claim);
    }

    public String extractName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean istokenExpired(String token) {
        return (extractExpiration(token).before(new Date()));
    }

}
