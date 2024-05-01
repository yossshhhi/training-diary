package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;

import java.util.Collections;
import java.util.List;

public class ExtraOptionDAO implements ExtraOptionRepository {
    private final DatabaseManager databaseManager;
    private final ResultSetMapper<ExtraOption> resultSetMapper;

    public ExtraOptionDAO(DatabaseManager databaseManager, ResultSetMapper<ExtraOption> resultSetMapper) {
        this.databaseManager = databaseManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public ExtraOption save(ExtraOption extraOption) {
        long generatedKey = databaseManager.executeUpdate("""
                INSERT INTO diary_schema.extra_option (workout_id, type_id, value)
                VALUES (?, ?, ?)
                """, extraOption.getWorkoutId(), extraOption.getTypeId(), extraOption.getValue());
        extraOption.setId(generatedKey);
        return extraOption;
    }

    @Override
    public List<ExtraOption> findAllByWorkoutId(Long workoutId) {
        return databaseManager.executeQuery("""
                SELECT o.*, t.name AS type_name
                FROM diary_schema.extra_option AS o
                JOIN diary_schema.extra_option_type AS t ON o.type_id = t.id
                WHERE workout_id = ?""", resultSetMapper, workoutId);
    }

    @Override
    public void deleteAll(List<ExtraOption> extraOptions) {
        List<Long> ids = extraOptions.stream()
                .map(ExtraOption::getId)
                .toList();
        Object[] params = ids.toArray(new Object[0]);
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = String.format("""
            DELETE FROM diary_schema.extra_option
            WHERE id IN (%s)
            """, placeholders);
        databaseManager.executeUpdate(sql, params);
    }
}
