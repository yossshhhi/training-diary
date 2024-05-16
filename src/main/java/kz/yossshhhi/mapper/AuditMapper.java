package kz.yossshhhi.mapper;

import kz.yossshhhi.dto.AuditDTO;
import kz.yossshhhi.model.Audit;
import org.mapstruct.Mapper;

/**
 * Mapper for converting {@link Audit} entities to {@link AuditDTO} data transfer objects.
 * Utilizes MapStruct for mapping fields between entity and DTO.
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {
    AuditDTO toDTO(Audit audit);
}
