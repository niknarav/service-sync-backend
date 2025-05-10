package com.example.servicesyncservice.service.task;

import com.example.servicesyncservice.dto.task.TaskResponsesList;
import com.example.servicesyncservice.mapper.task.TaskMapper;
import com.example.servicesyncservice.model.Task;
import com.example.servicesyncservice.repository.TaskRepository;
import com.example.servicesyncservice.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final OrderService orderService;

    private final TaskMapper taskMapper;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public TaskResponsesList getAllTasksByOrderId(Long id) {
        return taskMapper.entityListToResponseList(
                taskRepository.getAllByOrder(
                        orderService.findOrderById(id)
                ));
    }

}
