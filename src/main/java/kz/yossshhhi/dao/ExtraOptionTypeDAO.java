package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.ExtraOptionTypeRepository;
import kz.yossshhhi.model.ExtraOptionType;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;

import java.util.*;

public class ExtraOptionTypeDAO implements ExtraOptionTypeRepository {
    private final DatabaseManager databaseManager;
    private final ResultSetMapper<ExtraOptionType> resultSetMapper;

    public ExtraOptionTypeDAO(DatabaseManager databaseManager, ResultSetMapper<ExtraOptionType> resultSetMapper) {
        this.databaseManager = databaseManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public Optional<ExtraOptionType> findById(Long id) {
        List<ExtraOptionType> types = databaseManager.executeQuery(
                "SELECT * FROM diary_schema.extra_option_type WHERE id = ?", resultSetMapper, id);
        return types.isEmpty() ? Optional.empty() : Optional.ofNullable(types.get(0));
    }

    @Override
    public Optional<ExtraOptionType> findByName(String name) {
        List<ExtraOptionType> types = databaseManager.executeQuery(
                "SELECT * FROM diary_schema.extra_option_type WHERE name = ?", resultSetMapper, name);
        return types.isEmpty() ? Optional.empty() : Optional.ofNullable(types.get(0));
    }

    @Override
    public ExtraOptionType save(ExtraOptionType extraOptionType) {
        long generatedKey = databaseManager.executeUpdate("""
                INSERT INTO diary_schema.extra_option_type (name)
                VALUES (?)
                """, extraOptionType.getName());
        extraOptionType.setId(generatedKey);
        return extraOptionType;
    }

    @Override
    public List<ExtraOptionType> findAll() {
        return databaseManager.executeQuery(
                "SELECT * FROM diary_schema.extra_option_type", resultSetMapper);
    }
}
