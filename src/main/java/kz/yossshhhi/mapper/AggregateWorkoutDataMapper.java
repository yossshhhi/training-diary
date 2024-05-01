package kz.yossshhhi.mapper;

import kz.yossshhhi.dto.AggregateWorkoutDataDTO;
import kz.yossshhhi.model.AggregateWorkoutData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AggregateWorkoutDataMapper {
    AggregateWorkoutDataDTO toDTO(AggregateWorkoutData entity);
}
