package com.example.servicesyncservice.controller;

import com.example.servicesyncservice.dto.mechanic.MechanicResponse;
import com.example.servicesyncservice.dto.order.OrderResponse;
import com.example.servicesyncservice.dto.task.TaskResponse;
import com.example.servicesyncservice.dto.task.TaskResponsesList;
import com.example.servicesyncservice.dto.task.TaskStatusRequest;
import com.example.servicesyncservice.dto.task.UpsertTaskRequest;
import com.example.servicesyncservice.mapper.mechanic.MechanicMapper;
import com.example.servicesyncservice.mapper.order.OrderMapper;
import com.example.servicesyncservice.mapper.task.TaskMapper;
import com.example.servicesyncservice.model.Order;
import com.example.servicesyncservice.model.Task;
import com.example.servicesyncservice.model.TaskStatus;
import com.example.servicesyncservice.repository.OrderRepository;
import com.example.servicesyncservice.repository.TaskRepository;
import com.example.servicesyncservice.security.AppUserDetails;
import com.example.servicesyncservice.service.mechanic.MechanicService;
import com.example.servicesyncservice.service.order.OrderService;
import com.example.servicesyncservice.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/service-sync/order")
@RequiredArgsConstructor
public class TaskController {

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final TaskMapper taskMapper;

    private final MechanicMapper mechanicMapper;

    private final TaskService taskService;

    private final MechanicService mechanicService;

    private final TaskRepository taskRepository;

    @GetMapping("/mechanic/get/all")
    public ResponseEntity<List<MechanicResponse>> getAllMechanics() {
        return ResponseEntity.ok(mechanicService.findAllMechanics().stream()
                .map(mechanicMapper::entityToResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/mechanic/get/{id}")
    public ResponseEntity<MechanicResponse> getMechanicById(@PathVariable Long id) {
        return ResponseEntity.ok(mechanicMapper.entityToResponse(mechanicService.findMechanicById(id)));
    }

    @GetMapping("/mechanic/profile")
    public ResponseEntity<MechanicResponse> getMechanicProfile(@AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(mechanicMapper.entityToResponse(mechanicService.findMechanicById(userDetails.getId())));
    }

    @GetMapping("/mechanic/tasks")
    public ResponseEntity<TaskResponsesList> getAllTasksByMechanic(@AuthenticationPrincipal AppUserDetails userDetails) {
        Long mechanicId = userDetails.getId();
        return ResponseEntity.ok(taskService.getAllTasksByMechanicId(mechanicId));
    }

    @GetMapping("/mechanic/tasks/id/{id}")
    public ResponseEntity<TaskResponsesList> getAllTasksByMechanic(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getAllTasksByMechanicId(id));
    }



    @PostMapping("/add-task/{id}")
    public ResponseEntity<OrderResponse> addTaskToOrder(@PathVariable Long id, @RequestBody UpsertTaskRequest request) {
        Order order = orderService.findOrderById(id);

        Task task = taskMapper.requestToTask(request);
        task.setStartTime(LocalDateTime.now());
        task.setOrder(order);
        task.setMechanic(mechanicService.findMechanicById(request.getMechanicId()));
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.PENDING);
        task.setCost(request.getCost());
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

    @PutMapping("/task/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody UpsertTaskRequest request) {
        return ResponseEntity.ok(taskMapper.entityToResponse(taskService.updateTask(id, request)));
    }

    @PutMapping("/task/update/status/{id}")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusRequest status) {
        return ResponseEntity.ok(taskMapper.entityToResponse(taskService.updateStatus(id, status)));
    }

    @GetMapping("/task/get/status/{status}")
    public ResponseEntity<TaskResponsesList> getAllTasksByStatusAndMechanic(@PathVariable TaskStatus status,
                                                                 @AuthenticationPrincipal AppUserDetails userDetails) {
        System.out.println("Поиск по:" + status);
        TaskStatus taskStatus = TaskStatus.valueOf(status.name());

        return ResponseEntity.ok(taskService.getAllTasksByStatusAndMechanic(taskStatus, userDetails.getId()));
    }

    @GetMapping("/get-tasks/{id}")
    public ResponseEntity<TaskResponsesList> getAllTaskByOrder(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getAllTasksByOrderId(id));
    }

    @DeleteMapping("/task/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
