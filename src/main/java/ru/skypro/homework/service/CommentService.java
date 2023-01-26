package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.ResponseWrapperComment;

public interface CommentService {

    ResponseWrapperComment getAllCommentsForAdsWithId(Long adsId);

    CommentDto createNewComment(Long adsId, CommentDto commentDto);

    CommentDto getComments(long adPk, long id);

    void deleteComments(long adPk, long id);

    CommentDto updateComments(long adPk, long id, CommentDto commentDto);

}
