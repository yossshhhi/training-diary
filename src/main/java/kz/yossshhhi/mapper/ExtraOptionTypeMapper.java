package kz.yossshhhi.mapper;

import kz.yossshhhi.dto.ExtraOptionTypeDTO;
import kz.yossshhhi.model.ExtraOptionType;
import org.mapstruct.Mapper;

@Mapper
public interface ExtraOptionTypeMapper {
    ExtraOptionTypeDTO toDTO(ExtraOptionType entity);

    ExtraOptionType toEntity(ExtraOptionTypeDTO dto);
}
