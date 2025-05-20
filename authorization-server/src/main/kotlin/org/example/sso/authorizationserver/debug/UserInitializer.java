package org.example.sso.authorizationserver.debug;

import org.example.sso.authorizationserver.entity.Authority;
import org.example.sso.authorizationserver.entity.User;
import org.example.sso.authorizationserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 检查数据库中是否已有用户，如果没有则创建示例用户
        if (userRepository.count() == 0) {
            // 创建普通用户
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.addAuthority(new Authority(user.getUsername(), "ROLE_USER"));
            userRepository.save(user);

            // 创建管理员
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.addAuthority(new Authority(admin.getUsername(), "ROLE_USER"));
            admin.addAuthority(new Authority(admin.getUsername(), "ROLE_ADMIN"));
            userRepository.save(admin);

        }
    }
}
