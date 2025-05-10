package com.example.servicesyncservice.controller;

import com.example.servicesyncservice.dto.order.OrderResponse;
import com.example.servicesyncservice.dto.task.TaskResponsesList;
import com.example.servicesyncservice.dto.task.UpsertTaskRequest;
import com.example.servicesyncservice.mapper.order.OrderMapper;
import com.example.servicesyncservice.mapper.task.TaskMapper;
import com.example.servicesyncservice.model.Order;
import com.example.servicesyncservice.model.Task;
import com.example.servicesyncservice.repository.OrderRepository;
import com.example.servicesyncservice.service.order.OrderService;
import com.example.servicesyncservice.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/service-sync/order")
@RequiredArgsConstructor
public class TaskController {

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final TaskMapper taskMapper;

    private final TaskService taskService;

    @PostMapping("/add-task/{id}")
    public ResponseEntity<OrderResponse> addTaskToOrder(@PathVariable Long id, @RequestBody UpsertTaskRequest request) {
        Order order = orderService.findOrderById(id);

        Task task = taskMapper.requestToTask(request);
        task.setStartTime(LocalDateTime.now());
        task.setOrder(order);
        task = taskService.save(task);

        List<Task> tasks = order.getTasks();
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
        order.setTasks(tasks);

        orderRepository.save(order);

        return ResponseEntity.ok(orderMapper.entityToResponse(order));
    }

    @GetMapping("/get-tasks/{id}")
    public ResponseEntity<TaskResponsesList> getAllTaskByOrder(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getAllTasksByOrderId(id));
    }

}
