package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.WorkoutTypeRepository;
import kz.yossshhhi.dto.WorkoutTypeDTO;
import kz.yossshhhi.mapper.WorkoutTypeMapper;
import kz.yossshhhi.model.WorkoutType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Workout Type Service Tests")
class WorkoutTypeServiceTest {

    @Mock
    private WorkoutTypeRepository workoutTypeRepository;
    @Mock
    private WorkoutTypeMapper workoutTypeMapper;
    @InjectMocks
    private WorkoutTypeService workoutTypeService;

    @Test
    @DisplayName("Create New Workout Type - Success")
    void create_NewWorkoutType_Success() {
        WorkoutTypeDTO dto = new WorkoutTypeDTO(null, "TestType");
        WorkoutType workoutType = WorkoutType.builder().name("TestType").build();

        when(workoutTypeMapper.toEntity(dto)).thenReturn(workoutType);
        when(workoutTypeRepository.findByName("TestType")).thenReturn(Optional.empty());
        when(workoutTypeRepository.save(workoutType)).thenReturn(workoutType);

        WorkoutType createdWorkoutType = workoutTypeService.create(dto);

        assertNotNull(createdWorkoutType);
        assertEquals("TestType", createdWorkoutType.getName());
        verify(workoutTypeMapper, times(1)).toEntity(dto);
        verify(workoutTypeRepository, times(1)).findByName("TestType");
        verify(workoutTypeRepository, times(1)).save(workoutType);
    }

    @Test
    @DisplayName("Find By ID - Existing ID - Workout Type Returned")
    void findById_ExistingId_WorkoutTypeReturned() {
        WorkoutType existingType = WorkoutType.builder().id(1L).name("ExistingType").build();
        when(workoutTypeRepository.findById(1L)).thenReturn(Optional.of(existingType));

        WorkoutType foundWorkoutType = workoutTypeService.findById(1L);

        assertNotNull(foundWorkoutType);
        assertEquals("ExistingType", foundWorkoutType.getName());
        verify(workoutTypeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find By ID - Non-existing ID - Exception Thrown")
    void findById_NonExistingId_ExceptionThrown() {
        when(workoutTypeRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> workoutTypeService.findById(1L));

        assertEquals("Workout type not found", exception.getMessage());
        verify(workoutTypeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Exists By ID - Existing ID - True Returned")
    void existsById_ExistingId_TrueReturned() {
        when(workoutTypeRepository.findById(1L)).thenReturn(Optional.of(WorkoutType.builder().id(1L).build()));

        assertTrue(workoutTypeService.existsById(1L));
        verify(workoutTypeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Exists By ID - Non-existing ID - False Returned")
    void existsById_NonExistingId_FalseReturned() {
        when(workoutTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(workoutTypeService.existsById(1L));
        verify(workoutTypeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find All - No Workout Types - Empty List Returned")
    void findAll_NoWorkoutTypes_EmptyListReturned() {
        when(workoutTypeRepository.findAll()).thenReturn(Collections.emptyList());

        List<WorkoutTypeDTO> workoutTypes = workoutTypeService.findAll();

        assertNotNull(workoutTypes);
        assertTrue(workoutTypes.isEmpty());
        verify(workoutTypeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find All - Workout Types Available - All Workout Types Returned")
    void findAll_WorkoutTypesAvailable_AllWorkoutTypesReturned() {
        WorkoutType workoutType1 = WorkoutType.builder().id(1L).name("Type1").build();
        WorkoutType workoutType2 = WorkoutType.builder().id(2L).name("Type2").build();
        when(workoutTypeRepository.findAll()).thenReturn(List.of(workoutType1, workoutType2));

        List<WorkoutTypeDTO> workoutTypes = workoutTypeService.findAll();

        assertNotNull(workoutTypes);
        assertEquals(2, workoutTypes.size());
        verify(workoutTypeRepository, times(1)).findAll();
    }
}
