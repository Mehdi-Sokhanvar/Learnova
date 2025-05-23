package org.learnova.lms.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {


    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private Long validityInMs;


    private Algorithm algorithm() {
        return Algorithm.HMAC256(secretKey);
    }


    public String createToken(UserDetails user) {
        Date date = new Date();
        Date expiredTime = new Date(date.getTime() + validityInMs);
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(date)
                .withExpiresAt(expiredTime)
                .withClaim("role", user.getAuthorities().iterator().next().getAuthority())
                .sign(algorithm());
    }


    public String getUsername(String token) {
        return validateToken(token).getSubject();
    }

    public String getRole(String token) {
        return validateToken(token).getClaim("role").asString();
    }

    public DecodedJWT validateToken(String token) {
        return JWT.require(algorithm()).build().verify(token);
    }


}
