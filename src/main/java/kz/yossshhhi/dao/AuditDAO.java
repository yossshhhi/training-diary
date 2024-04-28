package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.AuditRepository;
import kz.yossshhhi.model.Audit;
import kz.yossshhhi.util.DatabaseManager;
import kz.yossshhhi.util.ResultSetMapper;

import java.util.List;

public class AuditDAO implements AuditRepository {
    private final DatabaseManager databaseManager;
    private final ResultSetMapper<Audit> resultSetMapper;

    public AuditDAO(DatabaseManager databaseManager, ResultSetMapper<Audit> resultSetMapper) {
        this.databaseManager = databaseManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public List<Audit> findAll() {
        return databaseManager.executeQuery("""
                SELECT a.*, u.username
                FROM diary_schema.audit AS a
                JOIN diary_schema.users AS u ON a.user_id = u.id""", resultSetMapper);
    }

    @Override
    public Audit save(Audit audit) {
        long generatedKey = databaseManager.executeUpdate(
                """
                        INSERT INTO diary_schema.audit (created_at, user_id, audit_action, audit_type)
                        VALUES (?, ?, ?, ?)
                         """,
                audit.getCreatedAt(), audit.getUserId(), audit.getAuditAction().toString(), audit.getAuditType().toString());
        audit.setId(generatedKey);
        return audit;
    }
}
