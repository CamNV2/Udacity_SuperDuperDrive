package com.udacity.jwdnd.course1.cloudstorage.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class Credentials {
    private Integer credentialId;
    private String url;
    private String username;
    private String salt;
    private String password;
    private Integer userId;
    private String decryptValue;

    public Credentials(Integer credentialId, String url, String username, String salt, String password, Integer userId) {
        this.credentialId = credentialId;
        this.url = url;
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.userId = userId;
    }
}