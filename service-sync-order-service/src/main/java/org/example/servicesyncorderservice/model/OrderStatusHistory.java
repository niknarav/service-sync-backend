package org.example.servicesyncorderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order-status-history")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String status;
    private Date changedAt;

    @ManyToOne
    @JoinColumn(name = "changed_by")
    private User changedBy;
}
