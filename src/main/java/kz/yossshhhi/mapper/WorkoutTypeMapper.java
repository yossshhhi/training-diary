package kz.yossshhhi.mapper;

import kz.yossshhhi.dto.WorkoutTypeDTO;
import kz.yossshhhi.model.WorkoutType;
import org.mapstruct.Mapper;

@Mapper
public interface WorkoutTypeMapper {

    WorkoutTypeDTO toDTO(WorkoutType entity);

    WorkoutType toEntity(WorkoutTypeDTO dto);
}
