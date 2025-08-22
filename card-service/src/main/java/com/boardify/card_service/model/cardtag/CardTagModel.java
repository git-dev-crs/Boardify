package com.boardify.card_service.model.cardtag;


import com.boardify.card_service.dto.response.CardTagDto;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "card_tag")
@AllArgsConstructor
@NoArgsConstructor
public class CardTagModel {
    @EmbeddedId
    CardTagId id;
    Long addedBy;
    LocalDateTime addedAt;

    public CardTagModel(CardTagId id, Long addedBy) {
        this.id = id;
        this.addedBy = addedBy;
        this.addedAt = LocalDateTime.now();
    }

    public CardTagDto toDto() {
        return new CardTagDto(
                id.cardId,
                id.title,
                addedBy,
                addedAt
        );
    }
}
