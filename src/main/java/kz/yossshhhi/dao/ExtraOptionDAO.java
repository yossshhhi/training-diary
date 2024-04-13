package kz.yossshhhi.dao;

import kz.yossshhhi.dao.repository.ExtraOptionRepository;
import kz.yossshhhi.model.ExtraOption;

import java.util.*;

public class ExtraOptionDAO implements ExtraOptionRepository {
    private final Map<Long, ExtraOption> extraOptions;
    private Long id;

    public ExtraOptionDAO() {
        this.extraOptions = new LinkedHashMap<>();
        this.id = 0L;

        extraOptions.put(++id, new ExtraOption(id, "Repetitions"));
        extraOptions.put(++id, new ExtraOption(id, "Distance covered"));
    }

    @Override
    public Optional<ExtraOption> findById(Long id) {
        return Optional.ofNullable(extraOptions.get(id));
    }

    @Override
    public Optional<ExtraOption> findByName(String name) {
        return extraOptions.values().stream()
                .filter(option -> option.getName().equals(name))
                .findFirst();
    }

    @Override
    public ExtraOption save(ExtraOption extraOption) {
        extraOption.setId(++id);
        extraOptions.put(extraOption.getId(), extraOption);
        return extraOption;
    }

    @Override
    public List<ExtraOption> findAll() {
        return new ArrayList<>(extraOptions.values());
    }

    @Override
    public Long getCount() {
        return id;
    }
}
