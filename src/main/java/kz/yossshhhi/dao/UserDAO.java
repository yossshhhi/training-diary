package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.UserRepository;
import kz.yossshhhi.model.User;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;

import java.util.List;
import java.util.Optional;

public class UserDAO implements UserRepository {
    private final DatabaseManager databaseManager;
    private final ResultSetMapper<User> resultSetMapper;

    public UserDAO(DatabaseManager databaseManager, ResultSetMapper<User> resultSetMapper) {
        this.databaseManager = databaseManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = databaseManager.executeQuery(
                "SELECT * FROM diary_schema.users WHERE id = ?", resultSetMapper, id);
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = databaseManager.executeQuery(
                "SELECT * FROM diary_schema.users WHERE username = ?", resultSetMapper, username);
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    @Override
    public User save(User user) {
        long generatedKey = databaseManager.executeUpdate("""
                INSERT INTO diary_schema.users (username, password, role)
                VALUES (?, ?, ?)
                """, user.getUsername(), user.getPassword(), user.getRole().toString());
        user.setId(generatedKey);
        return user;
    }
}
