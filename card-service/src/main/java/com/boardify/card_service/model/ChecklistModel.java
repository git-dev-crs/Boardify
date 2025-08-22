package com.boardify.card_service.model;

import com.boardify.card_service.dto.response.ChecklistDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "checklist")
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String text;
    Integer seqNo;
    @Column(name = "card_id")
    Long cardId;
    Long createdBy;
    LocalDateTime createdAt;
    Boolean isChecked;
    Long checkedBy;
    LocalDateTime checkedAt;

    public ChecklistModel(String text, Integer seqNo, Long cardId, Long createdBy){
        this.text = text;
        this.seqNo = seqNo;
        this.cardId = cardId;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    public ChecklistDto toDto(){
        return new ChecklistDto(
                id,
                text,
                seqNo,
                cardId,
                createdBy,
                createdAt,
                isChecked,
                checkedBy,
                checkedAt
        );
    }
}
