package com.boardify.list_service.model;

import com.boardify.list_service.dto.response.ListDto;
import com.boardify.list_service.dto.response.ListWithCardDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "list")
@NoArgsConstructor
@AllArgsConstructor
public class ListModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    Long boardId;
    Long createdBy;
    LocalDateTime createdAt;

    public ListModel(Long boardId, String title, String description, Long createdBy){
        this.boardId = boardId;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    public ListDto toListDto(){
        return new ListDto(
                id,
                title,
                description,
                boardId,
                createdBy,
                createdAt
        );
    }

    public ListWithCardDto toListWithCardDto(){
        return new ListWithCardDto(
                id,
                title,
                description,
                boardId,
                createdBy,
                createdAt,
                List.of()
        );
    }
}
