package com.example.servicesyncservice.mapper.task;

import com.example.servicesyncservice.dto.task.TaskResponse;
import com.example.servicesyncservice.dto.task.TaskResponsesList;
import com.example.servicesyncservice.dto.task.UpsertTaskRequest;
import com.example.servicesyncservice.mapper.mechanic.MechanicMapper;
import com.example.servicesyncservice.mapper.order.OrderMapper;
import com.example.servicesyncservice.model.Task;
import com.example.servicesyncservice.service.mechanic.MechanicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private OrderMapper orderMapper;

    private final MechanicService mechanicService;

    private MechanicMapper mechanicMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setMechanicMapper(MechanicMapper mechanicMapper) {
        this.mechanicMapper = mechanicMapper;
    }

    public Task requestToTask(UpsertTaskRequest request) {
        return Task.builder()
                .mechanic(mechanicService.findMechanicById(request.getMechanicId()))
                .description(request.getDescription())
                .cost(request.getCost())
                .build();
    }

    public TaskResponse entityToResponse(Task task) {

        return TaskResponse.builder()
                .orderId(task.getOrder() != null ? task.getOrder().getId() : null)
                .mechanic(mechanicMapper.entityToResponse(task.getMechanic()))
                .description(task.getDescription())
                .status(task.getStatus())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .cost(task.getCost())
                .build();
    }

    public TaskResponsesList entityListToResponseList(List<Task> list) {
        return TaskResponsesList.builder()
                .list(list.stream().map(this::entityToResponse).toList())
                .build();
    }

}
