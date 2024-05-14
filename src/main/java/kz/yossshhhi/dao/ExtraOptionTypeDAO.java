package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.ExtraOptionTypeRepository;
import kz.yossshhhi.model.ExtraOptionType;
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
public class ExtraOptionTypeDAO implements ExtraOptionTypeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ResultSetMapper<ExtraOptionType> resultSetMapper;

    private final String FIND_BY_ID_SQL = "SELECT * FROM diary_schema.extra_option_type WHERE id = ?";
    private final String FIND_BY_NAME_SQL = "SELECT * FROM diary_schema.extra_option_type WHERE name = ?";
    private final String SAVE_SQL = "INSERT INTO diary_schema.extra_option_type (name) VALUES (?)";
    private final String FIND_ALL_SQL = "SELECT * FROM diary_schema.extra_option_type";

    @Override
    public Optional<ExtraOptionType> findById(Long id) {
        List<ExtraOptionType> types = jdbcTemplate.query(FIND_BY_ID_SQL, resultSetMapper, id);
        return types != null && !types.isEmpty() ? Optional.of(types.get(0)) : Optional.empty();
    }

    @Override
    public Optional<ExtraOptionType> findByName(String name) {
        List<ExtraOptionType> types = jdbcTemplate.query(FIND_BY_NAME_SQL, resultSetMapper, name);
        return types != null && !types.isEmpty() ? Optional.of(types.get(0)) : Optional.empty();
    }

    @Override
    public ExtraOptionType save(ExtraOptionType extraOptionType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, extraOptionType.getName());
            return ps;
        }, keyHolder);

        Long generatedKey = (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        if (generatedKey != null) {
            extraOptionType.setId(generatedKey);
        } else {
            throw new RuntimeException("Failed to retrieve auto-generated key");
        }
        return extraOptionType;
    }

    @Override
    public List<ExtraOptionType> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, resultSetMapper);
    }
}
