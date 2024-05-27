package kz.yossshhhi.mapper;

import kz.yossshhhi.dto.ExtraOptionDTO;
import kz.yossshhhi.dto.WorkoutDTO;
import kz.yossshhhi.model.ExtraOption;
import kz.yossshhhi.model.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface for converting between entity objects and Data Transfer Objects (DTOs).
 */
@Mapper(componentModel = "spring")
public interface WorkoutMapper {
    WorkoutDTO toDTO(Workout entity);

    List<ExtraOptionDTO> toDTOList(List<ExtraOption> extraOptions);

    @Mapping(target = "userId", ignore = true)
    Workout toEntity(WorkoutDTO dto);

    @Mapping(target = "workoutId", ignore = true)
    @Mapping(target = "id", ignore = true)
    ExtraOption toExtraOption(ExtraOptionDTO dto);
}
