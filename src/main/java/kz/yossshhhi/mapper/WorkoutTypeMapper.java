package kz.yossshhhi.mapper;

import kz.yossshhhi.dto.WorkoutTypeDTO;
import kz.yossshhhi.model.WorkoutType;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between {@link WorkoutType} entity and {@link WorkoutTypeDTO} data transfer object.
 */
@Mapper
public interface WorkoutTypeMapper {

    WorkoutTypeDTO toDTO(WorkoutType entity);

    WorkoutType toEntity(WorkoutTypeDTO dto);
}
