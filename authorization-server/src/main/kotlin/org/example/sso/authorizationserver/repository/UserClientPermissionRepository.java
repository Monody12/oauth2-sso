package org.example.sso.authorizationserver.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Consumer;

@Repository
public class UserClientPermissionRepository {

    private final Map<String, Collection<String>> userClientPermissions = new HashMap<>();

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @PostConstruct
    public void init() {
        var map = new HashMap<String, Collection<String>>();
        map.put("user", Arrays.asList("client-1", "client-3"));
        map.put("admin", Arrays.asList("client-1", "client-2", "client-3"));
        map.forEach((username, allowClientName) -> {
            var set = new HashSet<String>();
            allowClientName.forEach(clientName -> {
                var registeredClientId = registeredClientRepository.findByClientId(clientName).getId();
                set.add(registeredClientId);
            });
            userClientPermissions.put(username, set);
        });
    }

    /**
     * 校验用户是否有对应客户端的访问权限
     * @param principalName
     * @param registeredClientId 客户端id，是一个uuid
     * @return
     */
    public boolean hasPermission(String principalName, String registeredClientId) {
        Collection<String> clients = userClientPermissions.get(principalName);
        return clients != null && clients.contains(registeredClientId);
    }
}
