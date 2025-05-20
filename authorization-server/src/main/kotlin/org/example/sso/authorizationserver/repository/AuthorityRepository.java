package org.example.sso.authorizationserver.repository;

import org.example.sso.authorizationserver.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Query("SELECT a.authority FROM Authority a WHERE a.username = :username")
    Set<String> findAuthoritiesByUsername(@Param("username") String username);
}
