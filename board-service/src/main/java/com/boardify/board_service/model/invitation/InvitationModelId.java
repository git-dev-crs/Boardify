package com.boardify.board_service.model.invitation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class InvitationModelId implements Serializable {
    @Column(name = "board_id")
    Long boardId;
    Long invitedUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvitationModelId that = (InvitationModelId) o;
        return Objects.equals(boardId, that.boardId) &&
                Objects.equals(invitedUser, that.invitedUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, invitedUser);
    }
}
