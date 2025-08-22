package com.boardify.card_service.service;

import com.boardify.card_service.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class DeletionHelper {
    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional
    public void deleteCardAndChildren(Long cardId) {
        checklistRepository.deleteAllByCardId(cardId);
        deleteCommentsByCardId(cardId);
        cardMemberRepository.deleteAllByCardId(cardId);
        cardTagRepository.deleteAllByCardId(cardId);

        entityManager.flush();
        entityManager.clear();

        cardRepository.deleteById(cardId);
    }

    @Transactional
    public void deleteCommentsByCardId(Long cardId){
        List<Long> commentIds = commentRepository.getCommentIdsByCardId(cardId);

        if (commentIds == null || commentIds.isEmpty()) return;

        commentRepository.deleteAllByTaggedIds(commentIds);

        commentRepository.deleteAllByIds(commentIds);
    }
}