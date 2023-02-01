package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.ResponseWrapperComment;
import ru.skypro.homework.entity.Comment;

public interface CommentService {

    ResponseWrapperComment getAllCommentsForAdsWithId(Long adsId);

    CommentDto createNewComment(Long adsId, CommentDto commentDto);

    CommentDto getComments(long adPk, long id);

    void deleteComments(long adPk, long id,  Authentication authentication);

    CommentDto updateComments(long adPk, long id, CommentDto commentDto, Authentication authentication);

    Comment getCommentByIdAndAuthorId(long adPk, long id);
}
