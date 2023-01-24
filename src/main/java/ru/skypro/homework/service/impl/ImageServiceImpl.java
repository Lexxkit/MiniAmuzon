package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exceptions.EmptyFileException;
import ru.skypro.homework.exceptions.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public byte[] updateAdsImage(long id, MultipartFile file) {
        log.info("Was invoked findAllAds method from {}", ImageService.class.getSimpleName());
        Image oldImage = imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
        extractInfoFromFile(file, oldImage);
        Image savedImage = imageRepository.save(oldImage);
        return savedImage.getData();
    }

    @Override
    public Image createImage(MultipartFile file, Ads ads) {
        Image imageToSave = new Image();
        extractInfoFromFile(file, imageToSave);
        imageToSave.setAds(ads);
        return imageRepository.save(imageToSave);
    }

    private void extractInfoFromFile(MultipartFile file, Image imageToSave) {
        if (file.isEmpty()) {
            throw new EmptyFileException();
        }
        byte[] imageData;
        try {
            imageData = file.getBytes();
        } catch (IOException e) {
            log.error("Image has some problems and cannot be read");
            throw new RuntimeException("Problems with uploaded image");
        }
        imageToSave.setData(imageData);
        imageToSave.setFileSize(file.getSize());
        imageToSave.setMediaType(file.getContentType());
    }
}
