package com.boardify.board_service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveUserDto {
    Long boardId;
    Long userId;
    Long removerId;
}
