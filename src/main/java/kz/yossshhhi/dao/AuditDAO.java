package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.AuditRepository;
import kz.yossshhhi.model.Audit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AuditDAO implements AuditRepository {
    private final Map<Long, Audit> audits;
    private Long id;

    public AuditDAO() {
        this.audits = new LinkedHashMap<>();
        this.id = 0L;
    }


    @Override
    public List<Audit> findAll() {
        return new ArrayList<>(audits.values());
    }

    @Override
    public Audit save(Audit audit) {
        audit.setId(++id);
        audits.put(audit.getId(), audit);
        return audit;
    }
}
