package org.learnova.lms.config.jwt;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAKey;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private Long validityInMin;


    private HmacKey getHmacKey() {
        return new HmacKey(secretKey.getBytes(StandardCharsets.UTF_8));
    }



    public String generateToken(UserDetails userDetails) {
        try {
            JwtClaims claims = new JwtClaims();
            claims.setSubject(userDetails.getUsername());
            claims.setExpirationTimeMinutesInTheFuture(validityInMin);
            claims.setIssuedAtToNow();
            claims.setIssuer("Learn ova.com");
            claims.setClaim("sub", userDetails.getUsername());

            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            jws.setKey(getHmacKey());
            return jws.getCompactSerialization();


        } catch (Exception e) {
            throw new RuntimeException("Error creating Jwt token ", e);
        }
    }


    public JwtClaims validateToken(String token) {
        try {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(30)
                    .setRequireSubject()
                    .setVerificationKey(getHmacKey())
                    .build();

            return jwtConsumer.processToClaims(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }


    public String getRole(String token) {
        return (String) validateToken(token).getClaimValue("role");
    }

    public String getUserName(String token) {
        return (String) validateToken(token).getClaimValue("username");
    }

}
