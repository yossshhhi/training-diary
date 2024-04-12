package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.UserRepository;
import kz.yossshhhi.model.enums.Role;
import kz.yossshhhi.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserDAO implements UserRepository {
    private final Map<Long, User> users;
    private Long id;

    public UserDAO () {
        users = new HashMap<>();
        id = 0L;

        save(User.builder()
                .username("admin")
                .password("admin")
                .role(Role.ADMIN)
                .build());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public User save(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }
}
