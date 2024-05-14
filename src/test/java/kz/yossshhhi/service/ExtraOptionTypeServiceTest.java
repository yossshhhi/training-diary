package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionTypeRepository;
import kz.yossshhhi.dto.ExtraOptionTypeDTO;
import kz.yossshhhi.mapper.ExtraOptionTypeMapper;
import kz.yossshhhi.model.ExtraOptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Extra Option Service Tests")
class ExtraOptionTypeServiceTest {

    @Mock
    private ExtraOptionTypeRepository extraOptionTypeRepository;
    @Mock
    private ExtraOptionTypeMapper extraOptionTypeMapper;
    @InjectMocks
    private ExtraOptionTypeService extraOptionTypeService;

    @Test
    @DisplayName("Create Extra Option Type When Not Exists")
    void create_ExtraOptionTypeDoesNotExist_ShouldCreateExtraOptionType() {
        ExtraOptionTypeDTO extraOptionTypeDTO = new ExtraOptionTypeDTO(null,"New Option");
        ExtraOptionType extraOptionType = new ExtraOptionType(null,"New Option");

        when(extraOptionTypeMapper.toEntity(extraOptionTypeDTO)).thenReturn(extraOptionType);
        when(extraOptionTypeRepository.findByName(extraOptionTypeDTO.name())).thenReturn(Optional.empty());
        when(extraOptionTypeRepository.save(extraOptionType)).thenReturn(extraOptionType);

        ExtraOptionType createdExtraOptionType = extraOptionTypeService.create(extraOptionTypeDTO);

        assertNotNull(createdExtraOptionType);
        assertEquals(extraOptionType, createdExtraOptionType);
        verify(extraOptionTypeMapper, times(1)).toEntity(extraOptionTypeDTO);
        verify(extraOptionTypeRepository, times(1)).findByName(extraOptionType.getName());
        verify(extraOptionTypeRepository, times(1)).save(extraOptionType);
        verifyNoMoreInteractions(extraOptionTypeRepository);
    }

    @Test
    @DisplayName("Get All Extra Option Types")
    void getAllExtraOptionTypes_ShouldReturnList() {
        ExtraOptionType extraOptionType1 = new ExtraOptionType();
        extraOptionType1.setId(1L);
        extraOptionType1.setName("Option 1");

        ExtraOptionType extraOptionType2 = new ExtraOptionType();
        extraOptionType2.setId(2L);
        extraOptionType2.setName("Option 2");

        List<ExtraOptionType> extraOptionTypes = Arrays.asList(extraOptionType1, extraOptionType2);

        when(extraOptionTypeRepository.findAll()).thenReturn(extraOptionTypes);

        List<ExtraOptionTypeDTO> all = extraOptionTypeService.findAll();

        assertEquals(extraOptionTypes.size(), all.size());
        verify(extraOptionTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(extraOptionTypeRepository);
    }

}
