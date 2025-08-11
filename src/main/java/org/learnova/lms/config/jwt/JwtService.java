package org.learnova.lms.config.jwt;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.learnova.lms.util.KeyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

@Service
public class JwtService {

    public static final String TOKEN_TYPE = "token_type";
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    @Value("${app.security.jwt.refresh-token-expiration}")
    private long accessTokenExpiration;
    @Value("${app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public JwtService() throws Exception {
        this.privateKey = KeyUtils.loadPrivateKey("keys/private.pem");
        this.publicKey = KeyUtils.loadPublicKey("keys/public.pem");
    }

    public String generateAccessToken(final String username, String role)  {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, "ACCESS_TOKEN");
        return buildToken(username, claims, this.accessTokenExpiration,role);
    }

    public String generateRefreshToken(final String username, String role) {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, "REFRESH_TOKEN");
        return buildToken(username, claims, this.refreshTokenExpiration,role);
    }


    public String buildToken(final String username, final Map<String, Object> claims, final long expiration,String role) {
        try {
            JwtClaims ta = new JwtClaims();
            ta.setSubject(username);
            ta.setExpirationTimeMinutesInTheFuture(expiration);
            ta.setIssuedAtToNow();
            ta.setIssuer("Learn ova.com");
            ta.setClaim(claims.keySet().iterator().next(), claims);
            ta.setClaim("role", role);
            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(ta.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            jws.setKey(this.privateKey);
            return jws.getCompactSerialization();
        } catch (Exception e) {
            throw new RuntimeException("Error creating Jwt token ", e);
        }
    }


    public boolean isTokenExpired(final String token,final String expectedUsername) throws MalformedClaimException {
        final String username = extractUsername(token);
        return username.equals(expectedUsername) && !isTokenExpired(token);
    }

    public String extractUsername(final String token) throws MalformedClaimException {
        return extractClaims(token).getSubject().toString();
    }

    private boolean isTokenExpired(final String token) throws MalformedClaimException {
        return extractClaims(token).getExpirationTime().isBefore(NumericDate.now());
    }

    public JwtClaims extractClaims(String token) {
        try {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(30)
                    .setRequireSubject()
                    .setVerificationKey(this.publicKey)
                    .build();

            return jwtConsumer.processToClaims(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

}
