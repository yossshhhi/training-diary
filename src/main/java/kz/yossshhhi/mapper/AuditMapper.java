package kz.yossshhhi.mapper;

import kz.yossshhhi.dto.AuditDTO;
import kz.yossshhhi.model.Audit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuditMapper {
    AuditDTO toDTO(Audit audit);
}
