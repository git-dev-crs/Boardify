package com.boardify.list_service.controller;

import com.boardify.list_service.dto.ResponseWrapper;
import com.boardify.list_service.dto.request.CreateListDto;
import com.boardify.list_service.dto.request.GetListDto;
import com.boardify.list_service.dto.response.ListDto;
import com.boardify.list_service.dto.response.ListWithCardDto;
import com.boardify.list_service.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list/")
public class ListController {
    @Autowired
    ListService listService;

//    public
    @GetMapping("/details/{list_id}")
    public ResponseEntity<ResponseWrapper<ListDto>> getList(
            @PathVariable("list_id") Long listId,
            @RequestBody GetListDto body
            ){
        return listService.getList(listId, body);
    }

//    public
    @GetMapping("/{list_id}")
    public ResponseEntity<ResponseWrapper<ListWithCardDto>> getListWithCards(
            @PathVariable("list_id") Long listId,
            @RequestParam Long userId
    ){
        return listService.getListWithCards(listId, userId);
    }

//    private
    @GetMapping("/getListAndCardByBoardId/{boardId}")
    public ResponseEntity<ResponseWrapper<List<ListWithCardDto>>> getListAndCardByBoardId(
            @PathVariable("boardId") Long boardId
    ){
        return listService.getListAndCardByBoardId(boardId);
    }

//    private
    @GetMapping("/isListAndMemberValid/{list_id}/{user_id}")
    ResponseEntity<ResponseWrapper<Boolean>> isListAndMemberValid(
            @PathVariable("list_id") Long listId,
            @PathVariable("user_id") Long userId
    ){
        return listService.isListAndUserValid(listId, userId);
    }

//    private
    @GetMapping("/boardIdIfListAndMemberValid/{list_id}/{user_id}")
    ResponseEntity<ResponseWrapper<Long>> boardIdIfListAndMemberValid(
            @PathVariable("list_id") Long listId,
            @PathVariable("user_id") Long userId
    ){
        return listService.boardIdIfListAndMemberValid(listId, userId);
    }

//    private
    @GetMapping("/isListsAndUserInSameBoard/{list_id_1}/{list_id_2}/{user_id}")
    ResponseEntity<ResponseWrapper<Boolean>> isListsAndUserInSameBoard(
            @PathVariable("list_id_1") Long listId1,
            @PathVariable("list_id_2") Long listId2,
            @PathVariable("user_id") Long userId
    ){
        return listService.isListsAndUserInABoard(listId1, listId2, userId);
    }

//    public
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<String>> create(@RequestBody CreateListDto body){
        return listService.create(body);
    }

//    public
    @DeleteMapping("/deleteList/{list_id}")
    public ResponseEntity<ResponseWrapper<String>> deleteList(
            @PathVariable("list_id") Long listId,
            @RequestParam Long userId
    ){
        return listService.deleteList(listId, userId);
    }

//    private
    @DeleteMapping("/deleteListsFromBoard/{boardId}")
    public ResponseEntity<ResponseWrapper<String>> deleteListsFromBoard(
            @PathVariable("boardId") Long boardId
    ){
        return listService.deleteListsFromBoard(boardId);
    }
}
