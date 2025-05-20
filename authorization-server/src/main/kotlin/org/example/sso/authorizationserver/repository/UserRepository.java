package org.example.sso.authorizationserver.repository;

import org.example.sso.authorizationserver.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(value = "User.withAuthorities", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findByUsername(String username);
}
