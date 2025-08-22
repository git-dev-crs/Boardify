package com.boardify.card_service.controller;

import com.boardify.card_service.dto.ResponseWrapper;
import com.boardify.card_service.dto.request.*;
import com.boardify.card_service.dto.request.create.CreateCardDto;
import com.boardify.card_service.dto.response.CardDto;
import com.boardify.card_service.dto.response.CardTitleDto;
import com.boardify.card_service.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card/")
public class CardController {
    @Autowired
    CardService cardService;

//    public
    @GetMapping("/details/{cardId}")
    public ResponseEntity<ResponseWrapper<CardDto>> get(
            @PathVariable("cardId") Long cardId,
            @RequestParam("userId") Long userId
    ){
        return cardService.get(cardId, userId);
    }

//    private
    @GetMapping("/getTitlesInList/{listId}")
    public ResponseEntity<ResponseWrapper<List<CardTitleDto>>> getTitlesOfList(
            @PathVariable("listId") Long listId
    ){
        return cardService.getTitlesOfList(listId);
    }

//    public
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<CardDto>> create(@RequestBody CreateCardDto body){
        return cardService.create(body);
    }

//    public
    @PostMapping("/moveCard")
    public ResponseEntity<ResponseWrapper<String>> moveCard(@RequestBody MoveCardDto body){
        return cardService.moveCard(body);
    }

//    public
    @PostMapping("/joinCard")
    public ResponseEntity<ResponseWrapper<String>> joinCard(@RequestBody JoinCardDto body){
        return cardService.joinCard(body);
    }

//    public
    @PostMapping("/addComment")
    public ResponseEntity<ResponseWrapper<String>> addComment(@RequestBody AddCommentDto body){
        return cardService.addComment(body);
    }

//    public
    @PostMapping("/markChecklist")
    public ResponseEntity<ResponseWrapper<String>> markChecklist(@RequestBody MarkChecklistDto body){
        return cardService.markChecklist(body);
    }

//    public
    @PostMapping("/addTag")
    public ResponseEntity<ResponseWrapper<String>> addTag(@RequestBody AddTagDto body){
        return cardService.addTag(body);
    }

//    public
    @PostMapping("/leaveCard")
    public ResponseEntity<ResponseWrapper<String>> leaveCard(@RequestBody LeaveCardDto body){
        return cardService.leaveCard(body);
    }

//    public
    @DeleteMapping("/deleteCard/{cardId}")
    public ResponseEntity<ResponseWrapper<String>> deleteCard(
            @PathVariable("cardId") Long cardId,
            @RequestParam Long userId
    ){
        return cardService.deleteCard(cardId, userId);
    }

//    public
    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<ResponseWrapper<String>> deleteComment(
        @PathVariable("commentId") Long commentId,
        @RequestParam Long userId
    ){
        return cardService.deleteComment(commentId, userId);
    }

//    private
    @DeleteMapping("/deleteCardsByListId/{listId}")
    public ResponseEntity<ResponseWrapper<String>> deleteCardsByListId(
            @PathVariable("listId") Long listId
    ){
        return cardService.deleteCardsWithListId(listId);
    }
}
