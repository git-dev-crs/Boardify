package com.boardify.profile_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {
    Long id;
    String email;
    String firstName;
    String middleName;
    String lastName;
    LocalDateTime created_at;
}
