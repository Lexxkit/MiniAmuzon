package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final AdsService adsService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public ResponseWrapperComment getAllCommentsForAdsWithId(Long adsId) {
        Ads adsById = adsService.getAdsById(adsId);
        List<Comment> comments = adsById.getComments();
        return commentMapper.commentsListToResponseWrapperComment(comments.size(), comments);
    }

    @Override
    public CommentDto createNewComment(Long adsId, CommentDto commentDto) {
        log.info("Was invoked createNewComment method from {}", CommentService.class.getSimpleName());
        Ads adsById = adsService.getAdsById(adsId);
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setAds(adsById);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(savedComment);
    }

    @Override
    public CommentDto getComments(long adPk, long id) {
        Comment comment = commentRepository.findCommentByIdAndAuthorId(adPk, id)
                .orElseThrow(CommentNotFoundException::new);
        return commentMapper.commentToCommentDto(comment);
    }

    @Override
    public void deleteComments (long adPk, long id) {
        Comment comment = commentRepository.findCommentByIdAndAuthorId(adPk, id)
                .orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComments(long adPk, long id, CommentDto commentDto){
        Comment comment = commentRepository.findCommentByIdAndAuthorId(adPk, id)
                .orElseThrow(CommentNotFoundException::new);
        // TODO: 31.01.2023 Исправить на (commentDto.getText())
        comment.setText(comment.getText());
        commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }
}
