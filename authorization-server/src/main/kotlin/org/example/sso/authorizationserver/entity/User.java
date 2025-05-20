package org.example.sso.authorizationserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "User.withAuthorities", attributeNodes = @NamedAttributeNode("authorities"))
public class User {

    @Id
    @Column(name = "username")
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean enabled = true;

    // 一个用户可以有多个权限
     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     private Set<Authority> authorities;

    public void addAuthority(Authority authority) {
        if (this.authorities == null) {
            this.authorities = new HashSet<>();
        }
        this.authorities.add(authority);
        authority.setUsername(this.getUsername());
        authority.setUser(this);
    }

    public void removeAuthority(Authority authority) {
        if (this.authorities!= null) {
            this.authorities.remove(authority);
            authority.setUser(null);
        }
    }
}
