package org.example.sso.authorizationserver.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorityId implements Serializable {
    private String username;
    private String authority;
}