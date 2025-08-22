package com.boardify.card_service.service;

import com.boardify.card_service.dto.ResponseWrapper;
import com.boardify.card_service.dto.request.*;
import com.boardify.card_service.dto.request.create.CreateCardChecklistDto;
import com.boardify.card_service.dto.request.create.CreateCardDto;
import com.boardify.card_service.dto.response.CardDto;
import com.boardify.card_service.dto.response.CardTitleDto;
import com.boardify.card_service.dto.response.ChecklistDto;
import com.boardify.card_service.dto.response.CommentDto;
import com.boardify.card_service.feign.BoardInterface;
import com.boardify.card_service.feign.ListInterface;
import com.boardify.card_service.feign.ProfileInterface;
import com.boardify.card_service.model.CardModel;
import com.boardify.card_service.model.ChecklistModel;
import com.boardify.card_service.model.CommentModel;
import com.boardify.card_service.model.cardmember.CardMemberId;
import com.boardify.card_service.model.cardmember.CardMemberModel;
import com.boardify.card_service.model.cardtag.CardTagId;
import com.boardify.card_service.model.cardtag.CardTagModel;
import com.boardify.card_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardMemberRepository cardMemberRepository;
    @Autowired
    private CardTagRepository cardTagRepository;
    @Autowired
    private ChecklistRepository checklistRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    ProfileInterface profileInterface;
    @Autowired
    ListInterface listInterface;
    @Autowired
    BoardInterface boardInterface;

    @Autowired
    DeletionHelper deletionHelper;

    public ResponseEntity<ResponseWrapper<CardDto>> get(Long cardId, Long userId) {
        try {
            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            Optional<CardModel> cardModel = cardRepository.findById(cardId);
            if (cardModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Card does not exist."));

            ResponseWrapper<Boolean> isMemberValid = listInterface.isListAndMemberValid(cardModel.get().getListId(), userId);
            if (!isMemberValid.isSuccess())
                switch (isMemberValid.getError()) {
                    //check list exist
                    case "INVALID_LIST" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("List does not Exist."));
                    }
                    //check the member in the board in which the list is.
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board of which list if a part."));
                    }
                }

            CardDto cardDto = cardModel.get().toDto();
            cardDto.setComments(buildCommentHierarchy(cardDto.getComments()));
            return ResponseEntity.ok(ResponseWrapper.success(cardDto));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }


    public ResponseEntity<ResponseWrapper<List<CardTitleDto>>> getTitlesOfList(Long listId) {
        try {
            List<CardTitleDto> cardTitleDtos = cardRepository.getCardsByListId(listId);

            return ResponseEntity.ok(ResponseWrapper.success(cardTitleDtos));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<CardDto>> create(CreateCardDto body) {
        try {
            String trimmerTitle = body.getTitle().trim();
            if (trimmerTitle.isBlank())
                return ResponseEntity.status(400).body(ResponseWrapper.failure("Title can not be blank."));

            List<String> trimmedChecklists = List.of();
            if (body.getChecklists() != null) {
                trimmedChecklists = new ArrayList<>();
                for (CreateCardChecklistDto checklistDto : body.getChecklists()) {
                    String checklist = checklistDto.getText().trim();
                    if (checklist.isBlank())
                        return ResponseEntity.status(400).body(ResponseWrapper.failure("Can not create blank checklist."));
                    trimmedChecklists.add(checklist);
                }
            }

            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getCreatedBy());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));
            //check list exist
            //check the member in the board in which the list is.
            ResponseWrapper<Boolean> isListAndMemberValid = listInterface
                    .isListAndMemberValid(body.getListId(), body.getCreatedBy());
            if (!isListAndMemberValid.isSuccess())
                switch (isListAndMemberValid.getError()) {
                    //check list exist
                    case "INVALID_LIST" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("List does not Exist."));
                    }
                    //check the member in the board in which the list is.
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board of which list if a part."));
                    }
                }

            CardModel savedCard = cardRepository.save(
                    new CardModel(
                            body.getListId(),
                            trimmerTitle,
                            body.getDescription(),
                            body.getCreatedBy(),
                            body.getPriority()
                    )
            );
            List<ChecklistDto> checklistDto = new ArrayList<>();
            if (body.getChecklists() != null) {
                for (int i = 0; i < body.getChecklists().size(); i++) {
                    ChecklistModel checklistModel = checklistRepository.save(
                            new ChecklistModel(
                                    trimmedChecklists.get(i),
                                    i + 1,
                                    savedCard.getId(),
                                    body.getCreatedBy()
                            )
                    );
                    checklistDto.add(checklistModel.toDto());
                }
            }

            CardDto cardDto = savedCard.toDto();
            cardDto.setChecklists(checklistDto);
            return ResponseEntity.ok(ResponseWrapper.success(cardDto));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> moveCard(MoveCardDto body) {
        try {
            Optional<CardModel> cardModel = cardRepository.findById(body.getCardId());
            if (cardModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Card does not exist."));

            if (!Objects.equals(cardModel.get().getListId(), body.getInList()))
                return ResponseEntity.ok(ResponseWrapper.failure("Card is not in given list."));

            if (Objects.equals(body.getInList(), body.getToList()))
                return ResponseEntity.ok(ResponseWrapper.failure("Can't move to same list."));

            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getUserId());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            ResponseWrapper<Boolean> isListsAndUserInABoard = listInterface.isListsAndUserInSameBoard(body.getInList(), body.getToList(), body.getUserId());
            if (!isListsAndUserInABoard.isSuccess())
                switch (isListsAndUserInABoard.getError()) {
                    case "INVALID_LIST_1" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("In List is invalid."));
                    }
                    case "INVALID_LIST_2" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("To List is invalid."));
                    }
                    case "NOT_IN_SAME_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Lists are not is same board"));
                    }
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board."));
                    }
                }

            int rows = cardRepository.updateListId(body.getToList(), body.getCardId());
            if (rows == 1)
                return ResponseEntity.ok(ResponseWrapper.success("Card moved to list."));

            return ResponseEntity.ok(ResponseWrapper.failure("Unknown Error."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }


    public ResponseEntity<ResponseWrapper<String>> joinCard(JoinCardDto body) {
        try {
            Optional<CardModel> cardModel = cardRepository.findById(body.getCardId());
            if (cardModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Card does not exist."));

            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getUserId());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            if (body.getAddedBy() != null) {
                ResponseWrapper<Boolean> isAddedByValid = profileInterface.isUserValid(body.getAddedBy());
                if (isAddedByValid.isSuccess() && !isAddedByValid.getData())
                    return ResponseEntity.ok(ResponseWrapper.failure("Invalid Added By User."));
            }

            ResponseWrapper<Boolean> isListAndUserValid = listInterface.isListAndMemberValid(cardModel.get().getListId(), body.getUserId());
            if (!isListAndUserValid.isSuccess())
                switch (isListAndUserValid.getError()) {
                    //check list exist
                    case "INVALID_LIST" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("List does not Exist."));
                    }
                    //check the member in the board in which the list is.
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board of which list if a part."));
                    }
                }

            if (body.getAddedBy() != null) {
                ResponseWrapper<Boolean> isListAndAddedByUser = listInterface.isListAndMemberValid(cardModel.get().getListId(), body.getAddedBy());
                if (!isListAndAddedByUser.isSuccess())
                    switch (isListAndAddedByUser.getError()) {
                        //check list exist
                        case "INVALID_LIST" -> {
                            return ResponseEntity.ok(ResponseWrapper.failure("List does not Exist."));
                        }
                        //check the member in the board in which the list is.
                        case "INVALID_BOARD" -> {
                            return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                        }
                        case "INVALID_USER" -> {
                            return ResponseEntity.ok(ResponseWrapper.failure("Adding User does not have the access to board of which list if a part."));
                        }
                    }
            }

            if (body.getAddedBy() != null) {
                cardMemberRepository.save(
                        new CardMemberModel(
                                new CardMemberId(body.getCardId(), body.getUserId()),
                                body.getAddedBy()
                        )
                );

                return ResponseEntity.ok(ResponseWrapper.success("User added to the card."));
            }


            cardMemberRepository.save(
                    new CardMemberModel(
                            new CardMemberId(body.getCardId(), body.getUserId())
                    )
            );

            return ResponseEntity.ok(ResponseWrapper.success("User joined the card."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> addComment(AddCommentDto body) {
        try {
            if (body.getTaggedComment() != null) {
                Optional<CommentModel> commentModel = commentRepository.findById(body.getTaggedComment());
                if (commentModel.isEmpty())
                    return ResponseEntity.ok(ResponseWrapper.failure("Tagged Comment does not exist."));

                if (commentModel.get().getTaggedCommentId() != null)
                    return ResponseEntity.ok(ResponseWrapper.failure("Comment can not be added in tagged comment due to hierarchical constraints."));
            }

            Optional<CardModel> cardModel = cardRepository.findById(body.getCardId());
            if (cardModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Card does not exist."));

            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getAddedBy());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            ResponseWrapper<Boolean> isListAndUserValid = listInterface.isListAndMemberValid(cardModel.get().getListId(), body.getAddedBy());
            if (!isListAndUserValid.isSuccess())
                switch (isListAndUserValid.getError()) {
                    //check list exist
                    case "INVALID_LIST" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("List does not Exist."));
                    }
                    //check the member in the board in which the list is.
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board of which list if a part."));
                    }
                }

            if (body.getTaggedComment() != null) {
                commentRepository.save(
                        new CommentModel(
                                body.getComment(),
                                body.getCardId(),
                                body.getAddedBy(),
                                body.getTaggedComment()
                        )
                );

                return ResponseEntity.ok(ResponseWrapper.success("Comment Added."));
            }

            commentRepository.save(
                    new CommentModel(
                            body.getComment(),
                            body.getCardId(),
                            body.getAddedBy()
                    )
            );
            return ResponseEntity.ok(ResponseWrapper.success("Comment Added."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> markChecklist(MarkChecklistDto body) {
        try {
            Optional<ChecklistModel> checklistModel = checklistRepository.findById(body.getChecklistId());
            if (checklistModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Checklist does not exist."));

            Optional<CardModel> cardModel = cardRepository.findById(checklistModel.get().getCardId());
            if (cardModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Card does not exist."));

            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getUserId());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            ResponseWrapper<Boolean> isListAndUserValid = listInterface.isListAndMemberValid(cardModel.get().getListId(), body.getUserId());
            if (!isListAndUserValid.isSuccess())
                switch (isListAndUserValid.getError()) {
                    //check list exist
                    case "INVALID_LIST" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("List does not Exist."));
                    }
                    //check the member in the board in which the list is.
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board of which list if a part."));
                    }
                }

            int rows = checklistRepository.updateCheckStatus(
                    body.getStatus(),
                    body.getUserId(),
                    LocalDateTime.now(),
                    body.getChecklistId()
            );

            if (rows == 1)
                return ResponseEntity.ok(ResponseWrapper.success("Mark Status updated."));

            return ResponseEntity.ok(ResponseWrapper.failure("Unknown Error."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> addTag(AddTagDto body) {
        try {
            String trimmedTitle = body.getTag().trim();
            if (trimmedTitle.isBlank())
                return ResponseEntity.ok(ResponseWrapper.failure("Blank Tag."));

            Optional<CardModel> cardModel = cardRepository.findById(body.getCardId());
            if (cardModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Card does not exist."));

            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getUserId());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            ResponseWrapper<Long> boardIdWrapper = listInterface.boardIdIfListAndMemberValid(cardModel.get().getListId(), body.getUserId());
            if (!boardIdWrapper.isSuccess())
                switch (boardIdWrapper.getError()) {
                    //check list exist
                    case "INVALID_LIST" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("List does not Exist."));
                    }
                    //check the member in the board in which the list is.
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board of which list if a part."));
                    }
                }

            ResponseWrapper<Boolean> isTagInBoard = boardInterface.isBoardAndTagValid(boardIdWrapper.getData(), trimmedTitle);
            if (!isTagInBoard.isSuccess()) {
                switch (isTagInBoard.getError()) {
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_TAG" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("No such tag in board."));
                    }
                }
            }

            cardTagRepository.save(
                    new CardTagModel(
                            new CardTagId(body.getCardId(), trimmedTitle),
                            body.getUserId()
                    )
            );
            return ResponseEntity.ok(ResponseWrapper.success("Tag added to the board"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> leaveCard(LeaveCardDto body) {
        try {
            Optional<CardModel> cardModel = cardRepository.findById(body.getCardId());
            if (cardModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Card does not exist."));

            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(body.getUserId());
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            CardMemberId cardMemberId = new CardMemberId(body.getCardId(), body.getUserId());

            Optional<CardMemberModel> cardMemberModel = cardMemberRepository.findById(cardMemberId);
            if (cardMemberModel.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("User have not joined card."));

            cardMemberRepository.deleteById(cardMemberId);

            return ResponseEntity.ok(ResponseWrapper.success("User left the card."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public List<CommentDto> buildCommentHierarchy(List<CommentDto> commentDtos) {
        Map<Long, List<CommentDto>> repliesGrouped = commentDtos.stream()
                .filter(c -> c.getTaggedCommentId() != null)
                .collect(Collectors.groupingBy(CommentDto::getTaggedCommentId));

        commentDtos.stream()
                .filter(c -> c.getTaggedCommentId() == null)
                .forEach(parent -> {
                    List<CommentDto> replies = repliesGrouped.getOrDefault(parent.getId(), new ArrayList<>());
                    parent.setReplyComments(replies);
                });

        return commentDtos.stream()
                .filter(c -> c.getTaggedCommentId() == null)
                .collect(Collectors.toList());
    }

    public ResponseEntity<ResponseWrapper<String>> deleteCard(Long cardId, Long userId) {
        try {
            //a comment can be deleted by its by commenter only.
            Optional<CardModel> cardModelOptional = cardRepository.findById(cardId);
            if (cardModelOptional.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Card does not exist."));
            CardModel card = cardModelOptional.get();
            System.out.println(card);

            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            ResponseWrapper<Boolean> isListAndUserValid = listInterface.isListAndMemberValid(card.getListId(), userId);
            if (!isListAndUserValid.isSuccess())
                switch (isListAndUserValid.getError()) {
                    //check list exist
                    case "INVALID_LIST" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("List does not Exist."));
                    }
                    //check the member in the board in which the list is.
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board of which list if a part."));
                    }
                }


            deletionHelper.deleteCardAndChildren(cardId);
            return ResponseEntity.ok(ResponseWrapper.success("Card Deleted."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteComment(Long commentId, Long userId) {
        try {
            //a comment can be deleted by its by commenter only.
            Optional<CommentModel> comment = commentRepository.findById(commentId);
            if (comment.isEmpty())
                return ResponseEntity.ok(ResponseWrapper.failure("Comment does not exist."));

            //check user exist
            ResponseWrapper<Boolean> isUserValid = profileInterface.isUserValid(userId);
            if (isUserValid.isSuccess() && !isUserValid.getData())
                return ResponseEntity.ok(ResponseWrapper.failure("Invalid User."));

            Long listId = cardRepository.getListId(comment.get().getCardId());
            ResponseWrapper<Boolean> isListAndUserValid = listInterface.isListAndMemberValid(listId, userId);
            if (!isListAndUserValid.isSuccess())
                switch (isListAndUserValid.getError()) {
                    //check list exist
                    case "INVALID_LIST" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("List does not Exist."));
                    }
                    //check the member in the board in which the list is.
                    case "INVALID_BOARD" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("Board Does Not Exist."));
                    }
                    case "INVALID_USER" -> {
                        return ResponseEntity.ok(ResponseWrapper.failure("User does not have the access to board of which list if a part."));
                    }
                }

            commentRepository.deleteAllByTaggedId(commentId);
            commentRepository.deleteById(commentId);

            return ResponseEntity.ok(ResponseWrapper.success("Comment Deleted."));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }

    public ResponseEntity<ResponseWrapper<String>> deleteCardsWithListId(Long listId) {
        try {
            List<Long> cardIds = cardRepository.getCardIdsByListId(listId);

            for(Long cardId: cardIds){
                deletionHelper.deleteCardAndChildren(cardId);
            }

            return ResponseEntity.ok(ResponseWrapper.success("DELETION_SUCCESS"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(ResponseWrapper.failure("Internal Server Error"));
        }
    }
}