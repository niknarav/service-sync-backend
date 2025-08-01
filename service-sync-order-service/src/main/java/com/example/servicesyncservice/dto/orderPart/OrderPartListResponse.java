package com.example.servicesyncservice.dto.orderPart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPartListResponse {

    private List<OrderPartResponse> list;

}
