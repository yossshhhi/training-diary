package kz.yossshhhi.dao.repository;

import kz.yossshhhi.model.ExtraOptionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Extra Option Type Repository Test")
class ExtraOptionTypeRepositoryTest {

    @Mock
    private ExtraOptionTypeRepository extraOptionTypeRepository;

    @Test
    @DisplayName("Should find an ExtraOptionType by ID and return it")
    void testFindById() {
        Long id = 1L;
        ExtraOptionType expectedType = new ExtraOptionType(id, "Type A");
        when(extraOptionTypeRepository.findById(id)).thenReturn(Optional.of(expectedType));

        Optional<ExtraOptionType> found = extraOptionTypeRepository.findById(id);

        assertThat(found).isPresent()
                .contains(expectedType);
    }

    @Test
    @DisplayName("Should retrieve all ExtraOptionTypes from the repository")
    void testFindAll() {
        List<ExtraOptionType> expectedTypes = Arrays.asList(new ExtraOptionType(1L, "Type A"), new ExtraOptionType(2L, "Type B"));
        when(extraOptionTypeRepository.findAll()).thenReturn(expectedTypes);

        List<ExtraOptionType> foundTypes = extraOptionTypeRepository.findAll();

        assertThat(foundTypes).hasSize(2)
                .containsAll(expectedTypes);
    }

    @Test
    @DisplayName("Should save a new ExtraOptionType and return the persisted entity")
    void testSave() {
        ExtraOptionType newType = new ExtraOptionType(null, "Type C");
        ExtraOptionType savedType = new ExtraOptionType(3L, "Type C");
        when(extraOptionTypeRepository.save(newType)).thenReturn(savedType);

        ExtraOptionType result = extraOptionTypeRepository.save(newType);

        assertThat(result).isEqualToComparingFieldByField(savedType);
    }
}