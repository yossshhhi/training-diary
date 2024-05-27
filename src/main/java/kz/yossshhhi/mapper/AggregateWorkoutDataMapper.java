package kz.yossshhhi.mapper;

import kz.yossshhhi.dto.AggregateWorkoutDataDTO;
import kz.yossshhhi.model.AggregateWorkoutData;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting {@link AggregateWorkoutData} entities to {@link AggregateWorkoutDataDTO} data transfer objects.
 * This interface is typically used to abstract the mapping process between the database entity model and the data model
 * exposed to the client, enhancing modularity and separation of concerns.
 */
@Mapper(componentModel = "spring")
public interface AggregateWorkoutDataMapper {
    AggregateWorkoutDataDTO toDTO(AggregateWorkoutData entity);
}
