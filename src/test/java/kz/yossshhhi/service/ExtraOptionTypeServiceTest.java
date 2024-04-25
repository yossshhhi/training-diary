package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.ExtraOptionTypeRepository;
import kz.yossshhhi.model.ExtraOptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("Extra Option Service Tests")
class ExtraOptionTypeServiceTest {

    @Mock
    private ExtraOptionTypeRepository extraOptionTypeRepository;

    private ExtraOptionTypeService extraOptionTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        extraOptionTypeService = new ExtraOptionTypeService(extraOptionTypeRepository);
    }

    @Test
    @DisplayName("Create Extra Option Type When Not Exists")
    void create_ExtraOptionTypeDoesNotExist_ShouldCreateExtraOptionType() {
        ExtraOptionType extraOptionType = new ExtraOptionType();
        extraOptionType.setName("New Option");

        when(extraOptionTypeRepository.findByName(extraOptionType.getName())).thenReturn(Optional.empty());
        when(extraOptionTypeRepository.save(extraOptionType)).thenReturn(extraOptionType);

        ExtraOptionType createdExtraOptionType = extraOptionTypeService.create(extraOptionType);

        assertNotNull(createdExtraOptionType);
        assertEquals(extraOptionType, createdExtraOptionType);
        verify(extraOptionTypeRepository, times(1)).findByName(extraOptionType.getName());
        verify(extraOptionTypeRepository, times(1)).save(extraOptionType);
        verifyNoMoreInteractions(extraOptionTypeRepository);
    }

    @Test
    @DisplayName("Get Extra Option Types String")
    void getExtraOptionTypesToString_ShouldReturnStringRepresentation() {
        ExtraOptionType extraOptionType1 = new ExtraOptionType();
        extraOptionType1.setId(1L);
        extraOptionType1.setName("Option 1");

        ExtraOptionType extraOptionType2 = new ExtraOptionType();
        extraOptionType2.setId(2L);
        extraOptionType2.setName("Option 2");

        List<ExtraOptionType> extraOptionTypes = Arrays.asList(extraOptionType1, extraOptionType2);

        when(extraOptionTypeRepository.findAll()).thenReturn(extraOptionTypes);

        String result = extraOptionTypeService.getExtraOptionTypesToString();

        StringBuilder expected = new StringBuilder();
        for (ExtraOptionType extraOptionType : extraOptionTypes) {
            expected.append(extraOptionType.getId()).append(". ")
                    .append(extraOptionType.getName()).append("\n");
        }
        assertEquals(expected.toString(), result);
        verify(extraOptionTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(extraOptionTypeRepository);
    }

}
