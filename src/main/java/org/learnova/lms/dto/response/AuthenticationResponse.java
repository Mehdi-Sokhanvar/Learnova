package org.learnova.lms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.learnova.lms.dto.request.LoginDTO;

public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;


    public AuthenticationResponse(Builder b) {
        this.accessToken = b.accessToken;
        this.refreshToken = b.refreshToken;
        this.tokenType = b.tokenType;
    }

    public static class Builder {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("token_type")
        private String tokenType;


        public Builder(String accessToken) {
            this.accessToken = accessToken;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(this);
        }
    }
}
