package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;

public interface ImageService {
    byte[] updateAdsImage(long id, MultipartFile file, Authentication authentication);

    Image createImage(MultipartFile image, Ads ads);

    byte[] getAdsImage(Long id);
}
