package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.Audit;

import java.util.List;

/**
 * The AuditRepository interface provides methods for accessing and managing audit data.
 */
public interface AuditRepository {

    /**
     * Retrieves a list of all audits.
     *
     * @return A list of all audits.
     */
    List<Audit> findAll();

    /**
     * Saves an audit.
     *
     * @param audit The audit to be saved.
     * @return The saved audit.
     */
    Audit save(Audit audit);
}

