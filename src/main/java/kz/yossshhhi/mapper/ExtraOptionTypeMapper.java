package kz.yossshhhi.mapper;

import kz.yossshhhi.dto.ExtraOptionTypeDTO;
import kz.yossshhhi.model.ExtraOptionType;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between {@link ExtraOptionType} entity and {@link ExtraOptionTypeDTO} data transfer object.
 * This mapper simplifies the transformation of data formats used in different layers of the application,
 * facilitating easier data manipulation and encapsulation.
 */
@Mapper
public interface ExtraOptionTypeMapper {
    ExtraOptionTypeDTO toDTO(ExtraOptionType entity);

    ExtraOptionType toEntity(ExtraOptionTypeDTO dto);
}
