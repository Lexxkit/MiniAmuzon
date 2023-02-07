package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final AdsService adsService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserService userService;

    /**
     * Receive all comments for Ads by Ads id
     *
     * @param adsId
     * @return comments list
     */
    @Override
    public ResponseWrapperComment getAllCommentsForAdsWithId(Long adsId) {
        log.info("Was invoked getAllCommentsForAdsWithId method from {}", CommentService.class.getSimpleName());
        Ads adsById = adsService.getAdsById(adsId);
        List<Comment> comments = adsById.getComments();
        return commentMapper.commentsListToResponseWrapperComment(comments.size(), comments);
    }

    /**
     * Creating of new comment
     *
     * @param adsId
     * @param commentDto
     * @param authentication
     * @return comment created
     */
    @Override
    public CommentDto createNewComment(Long adsId, CommentDto commentDto, Authentication authentication) {
        log.info("Was invoked createNewComment method from {}", CommentService.class.getSimpleName());
        Ads adsById = adsService.getAdsById(adsId);
        User author = userService.getUser(authentication.getName());
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setAds(adsById);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(savedComment);
    }

    /**
     * Get comment by Ads id and comment id
     *
     * @param adPk
     * @param id
     * @return comment
     */
    @Override
    public CommentDto getComments(long adPk, long id) {
        log.info("Was invoked getComments by AdsId and comment id method from {}", CommentService.class.getSimpleName());
        Comment comment = getCommentById(id);
        return commentMapper.commentToCommentDto(comment);
    }

    /**
     * Delete comment from DB by id
     * The repository method is being used {@link CommentRepository#delete(Object)}
     *
     * @param adPk
     * @param id
     * @param authentication
     */
    @Override
    public void deleteComments (long adPk, long id,  Authentication authentication) {
        Comment comment = getCommentById(id);
        log.info("Comment to delete {} {}", comment.getId(), comment.getText());
        userService.checkIfUserHasPermissionToAlter(authentication, comment.getAuthor().getUsername());
        commentRepository.delete(comment);
    }

    /**
     * Receive comment by comment id with checking by ads id in DB, then update comment
     *
     * @param adPk
     * @param id
     * @param commentDto
     * @param authentication
     * @return comment update
     */
    @Override
    public CommentDto updateComments(long adPk, long id, CommentDto commentDto, Authentication authentication){
        Comment comment = getCommentById(id);
        log.info("Was invoked updateComment method from {}", CommentService.class.getSimpleName());
        userService.checkIfUserHasPermissionToAlter(authentication, comment.getAuthor().getUsername());
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }

    /**
     * Get comment by comment id
     *
     * @param id
     * @return comment
     * @throws CommentNotFoundException if comment wasn't found
     */
    @Override
    public Comment getCommentById(long id) {
        log.info("Was invoked getCommentById method from {}", CommentService.class.getSimpleName());
        return commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);
    }
}

