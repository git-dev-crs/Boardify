package com.boardify.profile_service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {
    public String firstName;
    public String middleName;
    public String lastName;
    public String email;
}
