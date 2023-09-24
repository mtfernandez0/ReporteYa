package com.cons.reporteya.dto;

import com.cons.reporteya.entity.User;
import lombok.*;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class UserDto {

    private Long id;
    private String first_name;

    private String last_name;

    private Date date_of_birth;

    private String email;

    public UserDto userToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .date_of_birth(user.getDate_of_birth())
                .email(user.getEmail())
                .build();
    }
}
