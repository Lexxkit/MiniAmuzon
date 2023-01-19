package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    private  final AdsService adsService;

    @Override
    public byte[] updateAdsImage(Long id, MultipartFile image) {
          Ads ads = adsService.getAdsById(id);
         return null;
    }
}
