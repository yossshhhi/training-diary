package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.AuditRepository;
import kz.yossshhhi.model.Audit;
import kz.yossshhhi.util.ResultSetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class AuditDAO implements AuditRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ResultSetMapper<Audit> resultSetMapper;

    private final String FIND_ALL_SQL = """
                SELECT a.*, u.username
                FROM diary_schema.audit AS a
                JOIN diary_schema.users AS u ON a.user_id = u.id""";
    private final String SAVE_SQL = """
                INSERT INTO diary_schema.audit (created_at, user_id, audit_action, audit_type)
                VALUES (?, ?, ?, ?)""";

    @Override
    public List<Audit> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, resultSetMapper);
    }

    @Override
    public Audit save(Audit audit) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(audit.getCreatedAt()));
            ps.setLong(2, audit.getUserId());
            ps.setString(3, audit.getAuditAction().toString());
            ps.setString(4, audit.getAuditType().toString());
            return ps;
        }, keyHolder);

        Long generatedKey = (Long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        if (generatedKey != null) {
            audit.setId(generatedKey);
        } else {
            throw new RuntimeException("Failed to retrieve auto-generated key");
        }
        return audit;
    }
}
