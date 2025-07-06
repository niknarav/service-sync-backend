package com.example.servicesyncservice.repository;

import com.example.servicesyncservice.model.Order;
import com.example.servicesyncservice.model.Task;
import com.example.servicesyncservice.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByMechanicId(Long mechanicId);
    List<Task> getAllByOrder(Order order);
    List<Task> findAllByStatusAndMechanicId(TaskStatus status, Long mechanicId);
}
