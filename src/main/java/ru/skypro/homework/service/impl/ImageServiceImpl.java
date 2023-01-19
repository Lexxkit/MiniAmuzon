package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
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
    public byte[] updateAdsImage(long id, MultipartFile file) throws IOException {
        log.info("Was invoked findAllAds method from {}", ImageService.class.getSimpleName());
        Image oldImage = imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
        byte[] imageData = new byte[0];
        if (!file.isEmpty()) {
            imageData = file.getBytes();

            oldImage.setData(imageData);
            oldImage.setFileSize(file.getSize());
            oldImage.setMediaType(file.getContentType());
            imageRepository.save(oldImage);
        }
        return imageData;
    }
}
