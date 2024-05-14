package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionTypeRepository;
import kz.yossshhhi.dto.ExtraOptionTypeDTO;
import kz.yossshhhi.mapper.ExtraOptionTypeMapper;
import kz.yossshhhi.model.ExtraOptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing {@link ExtraOptionType} entities.
 */
@Service
@RequiredArgsConstructor
public class ExtraOptionTypeService {

    /**
     * The repository for managing {@link ExtraOptionType} entities.
     */
    private final ExtraOptionTypeRepository extraOptionTypeRepository;
    private final ExtraOptionTypeMapper mapper;

    /**
     * Creates a new extra option type.
     *
     * @param dto the extra option type dto to create
     * @throws IllegalArgumentException if an extra option type with the same name already exists
     */
    public ExtraOptionType create(ExtraOptionTypeDTO dto) {
        ExtraOptionType extraOptionType = mapper.toEntity(dto);
        extraOptionTypeRepository.findByName(extraOptionType.getName()).ifPresent(option -> {
            throw new IllegalArgumentException("Extra option type with " + option.getName() + " name already exists");
        });
        return extraOptionTypeRepository.save(extraOptionType);
    }

    /**
     * Retrieves a string representation of all extra option types.
     *
     * @return A string containing the IDs and names of all extra option types.
     */
    public List<ExtraOptionTypeDTO> findAll() {
        return extraOptionTypeRepository.findAll().stream().map(mapper::toDTO).toList();
    }
}

