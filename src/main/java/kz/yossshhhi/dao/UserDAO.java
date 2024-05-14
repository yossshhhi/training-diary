package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.UserRepository;
import kz.yossshhhi.model.User;
import kz.yossshhhi.util.ResultSetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDAO implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ResultSetMapper<User> resultSetMapper;

    private final String FIND_BY_ID_SQL = "SELECT * FROM diary_schema.users WHERE id = ?";
    private final String FIND_BY_USERNAME_SQL = "SELECT * FROM diary_schema.users WHERE username = ?";
    private final String SAVE_SQL = "INSERT INTO diary_schema.users (username, password, role) VALUES (?, ?, ?)";

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query(FIND_BY_ID_SQL, resultSetMapper, id);
        return users != null && !users.isEmpty() ? Optional.of(users.get(0)) : Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = jdbcTemplate.query(FIND_BY_USERNAME_SQL, resultSetMapper, username);
        return users != null && !users.isEmpty() ? Optional.of(users.get(0)) : Optional.empty();
    }

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().toString());
            return ps;
        }, keyHolder);

        Long generatedKey = (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        if (generatedKey != null) {
            user.setId(generatedKey);
        } else {
            throw new RuntimeException("Failed to retrieve auto-generated key");
        }
        return user;
    }
}
