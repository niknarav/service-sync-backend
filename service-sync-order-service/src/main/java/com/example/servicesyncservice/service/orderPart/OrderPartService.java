package com.example.servicesyncservice.service.orderPart;

import com.example.servicesyncservice.dto.orderPart.UpsertOrderPartRequest;
import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.mapper.orderPart.OrderPartMapper;
import com.example.servicesyncservice.model.OrderPart;
import com.example.servicesyncservice.repository.OrderPartRepository;
import com.example.servicesyncservice.service.order.OrderService;
import com.example.servicesyncservice.service.part.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class OrderPartService {

    private final OrderService orderService;

    private final OrderPartMapper orderPartMapper;

    private final OrderPartRepository orderPartRepository;

    private final PartService partService;

    public OrderPart createOrderPart(UpsertOrderPartRequest request) {

        if(orderService.findOrderById(request.getOrderId()) == null) {
            throw new EntityNotFoundException(MessageFormat.format("Заказ с указаным id не был найден. Id: {0}", request.getOrderId()));
        }

        if(partService.findPartByName(request.getPartName()) == null) {
            throw new EntityNotFoundException(MessageFormat.format("Часть с указаным именем не была найдена. Имя: {0}", request.getPartName()));
        }

        return orderPartRepository.save(orderPartMapper.requestToEntity(request));
    }

}
