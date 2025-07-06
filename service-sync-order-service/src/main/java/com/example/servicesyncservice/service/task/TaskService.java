package com.example.servicesyncservice.service.task;

import com.example.servicesyncservice.dto.task.TaskResponsesList;
import com.example.servicesyncservice.dto.task.TaskStatusRequest;
import com.example.servicesyncservice.dto.task.UpsertTaskRequest;
import com.example.servicesyncservice.mapper.task.TaskMapper;
import com.example.servicesyncservice.model.Task;
import com.example.servicesyncservice.model.TaskStatus;
import com.example.servicesyncservice.repository.TaskRepository;
import com.example.servicesyncservice.service.mechanic.MechanicService;
import com.example.servicesyncservice.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final OrderService orderService;

    private final TaskMapper taskMapper;

    private final MechanicService mechanicService;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public TaskResponsesList getAllTasksByOrderId(Long id) {
        return taskMapper.entityListToResponseList(
                taskRepository.getAllByOrder(
                        orderService.findOrderById(id)
                ));
    }

    public Task updateTask(Long id, UpsertTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена"));

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getCost() != null) {
            task.setCost(request.getCost());
        }

        if (request.getMechanicId() != null) {
            task.setMechanic(mechanicService.findMechanicById(request.getMechanicId()));
        }

        return taskRepository.save(task);
    }

    public Task updateStatus(Long id, TaskStatusRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if(request.getTaskStatus() == TaskStatus.COMPLETED) {
            task.setEndTime(LocalDateTime.now());
        }
        task.setStatus(request.getTaskStatus());
        return taskRepository.save(task);
    }

    public TaskResponsesList getAllTasksByMechanicId(Long mechanicId) {
        return taskMapper.entityListToResponseList(taskRepository.findByMechanicId(mechanicId));
    }

    public TaskResponsesList getAllTasksByStatusAndMechanic(TaskStatus status, Long mechanicId) {
        List<Task> tasks = taskRepository.findAllByStatusAndMechanicId(status, mechanicId);
        return taskMapper.entityListToResponseList(tasks);
    }

}
