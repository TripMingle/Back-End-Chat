package org.example.backendchat.common.constants;

import lombok.Getter;

@Getter
public enum JwtConstants {

    ACCESS_TOKEN("access"),
    REFRESH_TOKEN("refresh");

    private String message;

    JwtConstants(String message) {
        this.message = message;
    }
}
