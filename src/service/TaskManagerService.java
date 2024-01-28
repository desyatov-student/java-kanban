package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import main.models.dto.EpicDto;
import main.models.dto.TaskDto;
import main.models.dto.SubtaskDto;
import main.models.dto.UpdateEpicDto;
import main.models.dto.UpdateSubtaskDto;
import main.models.dto.UpdateTaskDto;
import main.repository.TaskRepository;
import main.utils.IdentifierGenerator;
import main.utils.MappingUtils;

public class TaskManagerService {
    private IdentifierGenerator identifierGenerator;
    private TaskRepository taskRepository;
    private MappingUtils mappingUtils;

    // Task's methods
    public List<TaskDto> getAllTasks() {
        return taskRepository.getAllTasks().stream()
                .map(mappingUtils::mapToTaskDto)
                .collect(Collectors.toList());
    }
    public void updateEpic(UpdateTaskDto updateTaskDto) {

    }

    // Epic's methods
    public List<EpicDto> getAllEpics() {
        return new ArrayList<>();
    }

    public void updateEpic(UpdateEpicDto epicDto) {

    }

}
