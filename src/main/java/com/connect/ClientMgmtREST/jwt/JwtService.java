package com.connect.ClientMgmtREST.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.PublicJwk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {

    private static  final String SECRET_KEY = "41843AE9356E074CB1D7126C60603CDD984D8536772E92BB073E07FDF7DF7151995B5B3127E45E688C58D7052A99421917A4C5EC598F3F6471727CEFA2386D01";
    private static final long JWT_EXPIRATION = TimeUnit.MINUTES.toMillis(30);
//    private static final long REFRESH_EXPIRATION = TimeUnit.HOURS.toMillis(5);

    public String generateToken(UserDetails userDetails){
        return generateToken( new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        extraClaims.put("iss", "http://www.jwt.io");
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(JWT_EXPIRATION)))
                .signWith(generateKey())
                .compact();
    }


    private SecretKey generateKey(){
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);          // converts decodedKey into secretKey
    }

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);     // subject is email or username in this case
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt){
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername())) && !isJwtExpired(jwt);
    }

    public boolean isJwtExpired(String jwt){
        return extractExpiration(jwt).before(Date.from(Instant.now()));
    }

    private Date extractExpiration(String jwt){
        return extractClaim(jwt, Claims::getExpiration);
    }
}
