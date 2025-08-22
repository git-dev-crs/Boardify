package com.boardify.profile_service.model;

import com.boardify.profile_service.dto.response.UserDetailsDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String email;
    String firstName;
    String middleName;
    String lastName;
    LocalDateTime createdAt;
    LocalDateTime deletedAt;

    public UserModel(String email, String firstName, String middleName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.createdAt =  LocalDateTime.now();
    }

    public UserDetailsDto toDetailsDto(){
        return new UserDetailsDto(
                this.id,
                this.email,
                this.firstName,
                this.middleName,
                this.lastName,
                this.createdAt
        );
    }
}
