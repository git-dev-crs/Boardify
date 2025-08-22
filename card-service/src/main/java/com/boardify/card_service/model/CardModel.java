package com.boardify.card_service.model;

import com.boardify.card_service.dto.response.CardDto;
import com.boardify.card_service.dto.response.CardTitleDto;
import com.boardify.card_service.model.cardmember.CardMemberModel;
import com.boardify.card_service.model.cardtag.CardTagModel;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "card")
@NoArgsConstructor
@AllArgsConstructor
public class CardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long listId;
    String title;
    String description;
    Long createdBy;
    LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    PriorityEnum priority;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "card_id")
    List<CardMemberModel> members;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "card_id")
    List<ChecklistModel> checklists;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "card_id")
    List<CommentModel> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "card_id")
    List<CardTagModel> tags;

    public CardModel(Long listId, String title, String description, Long createdBy, @Nullable PriorityEnum priority) {
        this.listId = listId;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.priority = (priority == null) ? PriorityEnum.MEDIUM : priority;
    }

    public CardModel(Long listId, String title, String description, Long createdBy, PriorityEnum priority, List<ChecklistModel> checklists) {
        this.listId = listId;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.priority = priority;
        this.checklists = checklists;
    }

    public CardDto toDto() {
        return new CardDto(
                id,
                listId,
                title,
                description,
                createdBy,
                createdAt,
                priority,

                members != null
                        ? members.stream().map(CardMemberModel::toDto).toList()
                        : List.of(),

                checklists != null
                        ? checklists.stream().map(ChecklistModel::toDto).toList()
                        : List.of(),
                comments != null
                        ? comments.stream().map(CommentModel::toDto).toList()
                        : List.of(),
                tags != null
                        ? tags.stream().map(CardTagModel::toDto).toList()
                        : List.of()
        );
    }

    public CardTitleDto toTitleDto(){
        return new CardTitleDto(
                id,
                title,
                createdAt,
                priority
        );
    }
}
