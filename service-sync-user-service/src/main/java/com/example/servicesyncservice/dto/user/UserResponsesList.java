package com.example.servicesyncservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponsesList implements Serializable {

    @Builder.Default
    List<UserResponse> userResponses = new ArrayList<>();

}
