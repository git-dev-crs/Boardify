package com.boardify.card_service.model.cardmember;


import com.boardify.card_service.dto.response.CardMemberDto;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "card_member")
@NoArgsConstructor
@AllArgsConstructor
public class CardMemberModel {
    @EmbeddedId
    CardMemberId id;

    LocalDateTime joinedAt;
    Long addedBy;

    public CardMemberModel(CardMemberId id){
        this.id = id;
        joinedAt = LocalDateTime.now();
    }
    public CardMemberModel(CardMemberId id, Long addedBy){
        this.id = id;
        this.addedBy = addedBy;
        joinedAt = LocalDateTime.now();
    }

    public CardMemberDto toDto(){
        return new CardMemberDto(
                id.cardId,
                id.userId,
                joinedAt,
                addedBy
        );
    }
}
