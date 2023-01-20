package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    byte[] updateAdsImage(long id, MultipartFile file) throws IOException;
}
