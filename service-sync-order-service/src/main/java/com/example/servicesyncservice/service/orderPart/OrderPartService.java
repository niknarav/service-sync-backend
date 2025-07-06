package com.example.servicesyncservice.service.orderPart;

import com.example.servicesyncservice.dto.orderPart.OrderPartListResponse;
import com.example.servicesyncservice.dto.orderPart.UpsertOrderPartRequest;
import com.example.servicesyncservice.exception.EntityNotFoundException;
import com.example.servicesyncservice.kafka.dto.PartResponseDto;
import com.example.servicesyncservice.kafka.producer.KafkaMovementClient;
import com.example.servicesyncservice.mapper.orderPart.OrderPartMapper;
import com.example.servicesyncservice.model.MovementType;
import com.example.servicesyncservice.model.OrderPart;
import com.example.servicesyncservice.model.Part;
import com.example.servicesyncservice.repository.OrderPartRepository;
import com.example.servicesyncservice.service.order.OrderService;
import com.example.servicesyncservice.service.part.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class OrderPartService {

    private final OrderService orderService;

    private final OrderPartMapper orderPartMapper;

    private final OrderPartRepository orderPartRepository;

    private final PartService partService;

    private final KafkaMovementClient kafkaMovementClient;

    public OrderPart createOrderPart(UpsertOrderPartRequest request, PartResponseDto partDto) {

        if(orderService.findOrderById(request.getOrderId()) == null) {
            throw new EntityNotFoundException(MessageFormat.format("Заказ с указаным id не был найден. Id: {0}", request.getOrderId()));
        }

        OrderPart orderPart = orderPartMapper.requestToEntity(request);
        Part part;
        if(partDto != null) {
            part = Part.builder()
                    .name(request.getPartName())
                    .partNumber(request.getPartName())
                    .price(partDto.getPrice())
                    .quantity(request.getQuantityUsed())
                    .build();
            partService.savePart(part);
        } else {
            part = partService.findPartByName(request.getPartName());
        }
        orderPart.setPart(part);
        orderPart.setTotalCost(part.getPrice().multiply(new BigDecimal(part.getQuantity())));

        OrderPart savedOrderPart = orderPartRepository.save(orderPart);

        kafkaMovementClient.sendMovement(
                part.getId(),
                request.getQuantityUsed(),
                MovementType.OUTCOME,
                "Использовано в заказе #" + orderPart.getOrder().getId()
        );

        return savedOrderPart;
    }

    public OrderPartListResponse getAllByOrderId(Long orderId) {
        return orderPartMapper.entityListToResponseList(orderPartRepository.findAllByOrder(orderService.findOrderById(orderId)));
    }

    public void deleteOrderPart(Long id) {
        orderPartRepository.deleteById(id);
    }

}
