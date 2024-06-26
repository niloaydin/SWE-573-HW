package com.nilo.communityapplication.auth.config;

import com.nilo.communityapplication.auth.AuthenticationService;
import com.nilo.communityapplication.auth.TokenBlackListService;
import com.nilo.communityapplication.model.User;
import com.nilo.communityapplication.repository.TokenBlockListRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Log
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET_KEY="602dcd264447c05d84b93d9cc5cd6973d8af5d19d0830fed10b031dc6445c65f";
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private final TokenBlackListService blackListService;
    private final TokenBlockListRepository tokenBlockListRepository;

    public String extractEmail(String token){
        String email  = extractClaim(token, Claims::getSubject);
        logger.info("EMAIL IN EXTRACT EMAIL {}", email);
        return email;
    }

/*    public String generateToken(User userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }*/

    public String generateToken(User userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getEmail());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // expires after 24 hours
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isBlacklisted(String token) {
        return tokenBlockListRepository.existsByToken(token);
    }

    public boolean isTokenValid(String token, User userDetails){
        try {
            final String username = extractEmail(token);
            logger.warn("USERNAME IN ISTOKENVALID {}", username);
            return (username.equals(userDetails.getEmail())) && !isTokenExpired(token) && !isBlacklisted(token);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

