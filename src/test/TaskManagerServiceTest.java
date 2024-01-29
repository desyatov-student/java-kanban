package test;

import java.util.List;
import main.models.TaskStatus;
import main.models.dto.CreateEpicDto;
import main.models.dto.CreateSubtaskDto;
import main.models.dto.EpicDto;
import main.models.dto.UpdateEpicDto;
import main.models.dto.UpdateSubtaskDto;
import main.repository.TaskRepositoryInMemory;
import main.utils.IdentifierGenerator;
import main.utils.MappingUtils;
import org.junit.jupiter.api.Test;
import service.TaskManagerService;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerServiceTest {

    TaskManagerService taskManagerService() {
        return new TaskManagerService(
                new IdentifierGenerator(),
                new TaskRepositoryInMemory(),
                new MappingUtils()
        );
    }

    @Test
    void getNextTaskId() {
        TaskManagerService taskManager = taskManagerService();
        assertEquals(IdentifierGenerator.INITIAL_IDENTIFIER, taskManager.getNextTaskId());
        taskManager.getNextTaskId();
        taskManager.getNextTaskId();
        taskManager.getNextTaskId();
        assertEquals(5, taskManager.getNextTaskId());
    }

    @Test
    void createEpic() {
        TaskManagerService taskManager = taskManagerService();

        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());

        CreateEpicDto epicDto = new CreateEpicDto(1, "Epic1", "Desc1");
        taskManager.createEpic(epicDto);
        List<EpicDto> allEpics = taskManager.getAllEpics();

        System.out.println(taskManager.getAllEpics());

        CreateSubtaskDto createSubtaskDto = new CreateSubtaskDto(2, "subtask 1", "description");
        taskManager.createSubtask(createSubtaskDto, epicDto.getId());

        System.out.println(taskManager.getAllEpics());

        UpdateSubtaskDto updateSubtaskDto = new UpdateSubtaskDto(2, "subtask 1", "description", TaskStatus.DONE);
        taskManager.updateSubtask(updateSubtaskDto);

        CreateSubtaskDto createSubtaskDto2 = new CreateSubtaskDto(3, "subtask 2", "description");
        taskManager.createSubtask(createSubtaskDto2, epicDto.getId());

        System.out.println(taskManager.getAllEpics());
    }
}
