package com.boardify.list_service.service;


import com.boardify.list_service.dto.ResponseWrapper;
import com.boardify.list_service.dto.request.CreateListDto;
import com.boardify.list_service.dto.request.GetListDto;
import com.boardify.list_service.dto.response.CardTitleDto;
import com.boardify.list_service.dto.response.ListDto;
import com.boardify.list_service.dto.response.ListWithCardDto;
import com.boardify.list_service.feign.BoardInterface;
import com.boardify.list_service.feign.CardInterface;
import com.boardify.list_service.feign.ProfileInterface;
import com.boardify.list_service.model.ListModel;
import com.boardify.list_service.repository.ListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ListService {
    @Autowired
    ListRepository listRepository;

    @Autowired
    ProfileInterface profileInterface;
    @Autowired
    BoardInterface boardInterface;
    @Autowired
    CardInterface cardInterface;

    public ResponseEntity<ResponseWrapper<ListDto>> getList(Long listId, GetListDto body) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getUserId());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            //check if board is valid and user id part of it.
            ResponseWrapper<Boolean> res = boardInterface
                    .isBoardAndMemberValid(body.getBoardId(), body.getUserId());
            if (!res.isSuccess())
                switch (res.getError()) {
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board."));
                    }
                }
            Optional<ListModel> listModel = listRepository.findById(listId);
            //check the conditions
            if (listModel.isEmpty())
                return ResponseEntity.status(404).body(ResponseWrapper.failure("List Does Not Exist"));

            if (!Objects.equals(listModel.get().getBoardId(), body.getBoardId()))
                return ResponseEntity.ok(ResponseWrapper.failure("List is not a part of board."));


            ListDto listDto = listModel.get().toListDto();
            return ResponseEntity.ok(ResponseWrapper.success(listDto));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<ListWithCardDto>> getListWithCards(Long listId, Long userId) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            Optional<ListModel> listModel = listRepository.findById(listId);
            if (listModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("List does not exist."));

            //check if board is valid and user id part of it.
            ResponseWrapper<Boolean> res = boardInterface
                    .isBoardAndMemberValid(listModel.get().getBoardId(), userId);
            if (!res.isSuccess())
                switch (res.getError()) {
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board."));
                    }
                }

            ListWithCardDto listWithCardDto = listModel.get().toListWithCardDto();
            ResponseWrapper<List<CardTitleDto>> cards = cardInterface.getTitlesOfList(listId);
            listWithCardDto.setCards(cards.getData());

            return ResponseEntity.ok(ResponseWrapper.success(listWithCardDto));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<List<ListWithCardDto>>> getListAndCardByBoardId(Long boardId) {
        try {
            List<ListModel> lists = listRepository.getByBoardId(boardId);

            List<ListWithCardDto> listsWithCards = lists.stream()
                    .map(ListModel::toListWithCardDto)
                    .toList();

            for (ListWithCardDto list : listsWithCards) {
                list.setCards(cardInterface.getTitlesOfList(list.getId()).getData());
            }

            return ResponseEntity.ok(ResponseWrapper.success(listsWithCards));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> create(CreateListDto body) {
        try {
            String trimmedTitle = body.getTitle().trim();
            if (trimmedTitle.isBlank())
                return ResponseEntity.status(404).body(ResponseWrapper.failure("Invalid title"));

            //Check user id
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getCreatedBy());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            //check if board is valid and user id part of it.
            ResponseWrapper<Boolean> isBoardAndMemberValid = boardInterface
                    .isBoardAndMemberValid(body.getBoardId(), body.getCreatedBy());
            if (!isBoardAndMemberValid.isSuccess())
                switch (isBoardAndMemberValid.getError()) {
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board."));
                    }
                }
            listRepository.save(
                    new ListModel(
                            body.getBoardId(),
                            trimmedTitle,
                            body.getDescription(),
                            body.getCreatedBy()
                    )
            );
            return ResponseEntity.ok(ResponseWrapper.success("List created successfully"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500)
                    .body(new ResponseWrapper<>("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<Boolean>> isListAndUserValid(Long listId, Long userId) {
        try {
            Optional<ListModel> listModel = listRepository.findById(listId);
            if (listModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("INVALID_LIST"));

            ResponseWrapper<Boolean> isBoardAndMemberValid = boardInterface.isBoardAndMemberValid(listModel.get().getBoardId(), userId);

            return ResponseEntity.ok(isBoardAndMemberValid);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<Boolean>> isListsAndUserInABoard(Long listId1, Long listId2, Long userId) {
        try {
            Optional<ListModel> listModel1 = listRepository.findById(listId1);
            if (listModel1.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("INVALID_LIST_1"));

            Optional<ListModel> listModel2 = listRepository.findById(listId2);
            if (listModel2.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("INVALID_LIST_2"));

            if (!Objects.equals(listModel1.get().getBoardId(), listModel2.get().getBoardId()))
                return ResponseEntity.ok(ResponseWrapper.failure("NOT_IN_SAME_BOARD"));

            return ResponseEntity.ok(boardInterface.isBoardAndMemberValid(listModel1.get().getBoardId(), userId));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<Long>> boardIdIfListAndMemberValid(Long listId, Long userId) {
        try {
            Optional<ListModel> listModel = listRepository.findById(listId);
            if (listModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("INVALID_LIST"));

            ResponseWrapper<Boolean> isBoardAndMemberValid = boardInterface.isBoardAndMemberValid(listModel.get().getBoardId(), userId);

            if (isBoardAndMemberValid.isSuccess())
                return ResponseEntity.ok(ResponseWrapper.success(listModel.get().getBoardId()));
            else
                return ResponseEntity.ok(ResponseWrapper.failure(isBoardAndMemberValid.getError()));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteList(
            Long listId,
            Long userId
    ) {
        try {
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            Optional<ListModel> list = listRepository.findById(listId);
            if (list.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("List does not exist."));

            //check if board is valid and user id part of it.
            ResponseWrapper<Boolean> res = boardInterface
                    .isBoardAndMemberValid(list.get().getBoardId(), userId);
            if (!res.isSuccess())
                switch (res.getError()) {
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board."));
                    }
                }

            ResponseWrapper<String> response = cardInterface.deleteCommentByListId(listId);
            listRepository.deleteById(listId);

            if (response.isSuccess())
                return ResponseEntity.ok(ResponseWrapper.success("List deleted."));

            return ResponseEntity.ok(ResponseWrapper.success("Unknown Error"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteListsFromBoard(Long boardId) {
        try {
            List<Long> listIds = listRepository.getIdsByBoardId(boardId);

            for (Long listId : listIds) {
                ResponseWrapper<String> res = cardInterface.deleteCommentByListId(listId);
                if (!res.isSuccess())
                    return ResponseEntity.ok(res);

                listRepository.deleteById(listId);
            }

            return ResponseEntity.ok(ResponseWrapper.success("LIST_DELETED"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }
}
