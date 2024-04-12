package kz.yossshhhi.service;

import kz.yossshhhi.dao.repository.WorkoutTypeRepository;
import kz.yossshhhi.model.WorkoutType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutTypeServiceTest {

    @Mock
    private WorkoutTypeRepository workoutTypeRepository;

    private WorkoutTypeService workoutTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        workoutTypeService = new WorkoutTypeService(workoutTypeRepository);
    }

    @Test
    void create_NewWorkoutType_Success() {
        WorkoutType workoutType = WorkoutType.builder().name("TestType").build();
        when(workoutTypeRepository.findByName("TestType")).thenReturn(Optional.empty());
        when(workoutTypeRepository.save(workoutType)).thenReturn(workoutType);

        WorkoutType createdWorkoutType = workoutTypeService.create(workoutType);

        assertNotNull(createdWorkoutType);
        assertEquals("TestType", createdWorkoutType.getName());
        verify(workoutTypeRepository, times(1)).findByName("TestType");
        verify(workoutTypeRepository, times(1)).save(workoutType);
    }

    @Test
    void create_ExistingWorkoutType_ExceptionThrown() {
        WorkoutType existingType = WorkoutType.builder().id(1L).name("ExistingType").build();
        when(workoutTypeRepository.findByName("ExistingType")).thenReturn(Optional.of(existingType));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> workoutTypeService.create(WorkoutType.builder().name("ExistingType").build()));

        assertEquals("Workout type with ExistingType name already exists", exception.getMessage());
        verify(workoutTypeRepository, times(1)).findByName("ExistingType");
        verify(workoutTypeRepository, never()).save(any());
    }

    @Test
    void findById_ExistingId_WorkoutTypeReturned() {
        WorkoutType existingType = WorkoutType.builder().id(1L).name("ExistingType").build();
        when(workoutTypeRepository.findById(1L)).thenReturn(Optional.of(existingType));

        WorkoutType foundWorkoutType = workoutTypeService.findById(1L);

        assertNotNull(foundWorkoutType);
        assertEquals("ExistingType", foundWorkoutType.getName());
        verify(workoutTypeRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NonExistingId_ExceptionThrown() {
        when(workoutTypeRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> workoutTypeService.findById(1L));

        assertEquals("Workout type not found", exception.getMessage());
        verify(workoutTypeRepository, times(1)).findById(1L);
    }

    @Test
    void existsById_ExistingId_TrueReturned() {
        when(workoutTypeRepository.findById(1L)).thenReturn(Optional.of(WorkoutType.builder().id(1L).build()));

        assertTrue(workoutTypeService.existsById(1L));
        verify(workoutTypeRepository, times(1)).findById(1L);
    }

    @Test
    void existsById_NonExistingId_FalseReturned() {
        when(workoutTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(workoutTypeService.existsById(1L));
        verify(workoutTypeRepository, times(1)).findById(1L);
    }

    @Test
    void findAll_NoWorkoutTypes_EmptyListReturned() {
        when(workoutTypeRepository.findAll()).thenReturn(Collections.emptyList());

        List<WorkoutType> workoutTypes = workoutTypeService.findAll();

        assertNotNull(workoutTypes);
        assertTrue(workoutTypes.isEmpty());
        verify(workoutTypeRepository, times(1)).findAll();
    }

    @Test
    void findAll_WorkoutTypesAvailable_AllWorkoutTypesReturned() {
        WorkoutType workoutType1 = WorkoutType.builder().id(1L).name("Type1").build();
        WorkoutType workoutType2 = WorkoutType.builder().id(2L).name("Type2").build();
        when(workoutTypeRepository.findAll()).thenReturn(List.of(workoutType1, workoutType2));

        List<WorkoutType> workoutTypes = workoutTypeService.findAll();

        assertNotNull(workoutTypes);
        assertEquals(2, workoutTypes.size());
        assertTrue(workoutTypes.contains(workoutType1));
        assertTrue(workoutTypes.contains(workoutType2));
        verify(workoutTypeRepository, times(1)).findAll();
    }
}
