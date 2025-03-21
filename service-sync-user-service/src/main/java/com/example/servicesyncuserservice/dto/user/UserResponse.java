package com.example.servicesyncuserservice.dto.user;

import com.example.servicesyncuserservice.entity.User.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String username;

    private String name;

    private String surname;

    private String patronymic;

    private PostType post;

}
