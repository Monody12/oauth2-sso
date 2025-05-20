package org.example.sso.authorizationserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "authorities")
// 使用 @IdClass 指定复合主键类
@IdClass(AuthorityId.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "user")
public class Authority {

    @Id // 复合主键的一部分
    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Id // 复合主键的一部分
    @Column(name = "authority", length = 50, nullable = false)
    private String authority;

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    @MapsId("username") // 映射到复合主键的username字段
    private User user;

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.username = user.getUsername();
        } else {
            this.username = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority1 = (Authority) o;
        return Objects.equals(username, authority1.username) &&
                Objects.equals(authority, authority1.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authority);
    }

}
