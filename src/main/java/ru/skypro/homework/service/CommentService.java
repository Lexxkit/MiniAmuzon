package ru.skypro.homework.service;

import ru.skypro.homework.dto.ResponseWrapperComment;

public interface CommentService {

    ResponseWrapperComment getAllCommentsForAdsWithId(Long adsId);
}