package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exceptions.EmptyFileException;
import ru.skypro.homework.exceptions.ImageCanNotBeReadException;
import ru.skypro.homework.exceptions.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    /**
     * Receive old image by id, update and save.
     *
     * @param id identification number of an image
     * @param file {@link MultipartFile} with an image
     * @return byte array
     * @throws ImageNotFoundException if no image was found
     */
    @Override
    public byte[] updateAdsImage(long id, MultipartFile file) {
        log.info("Was invoked findAllAds method from {}", ImageService.class.getSimpleName());
        Image oldImage = getImageFromDB(id);
        extractInfoFromFile(file, oldImage);
        Image savedImage = imageRepository.save(oldImage);
        return savedImage.getData();
    }

    /**
     * Create new image for ads.
     *
     * @param file {@link MultipartFile} with an image
     * @param ads {@link Ads} instance
     * @return image created
     */
    @Override
    public Image createImage(MultipartFile file, Ads ads) {
        log.info("Was invoked createImage method from {}", ImageService.class.getSimpleName());
        Image imageToSave = new Image();
        extractInfoFromFile(file, imageToSave);
        imageToSave.setAds(ads);
        return imageRepository.save(imageToSave);
    }

    /**
     * Get image for ads by image id.
     *
     * @param id identification number of an image
     * @return byte array
     */
    @Override
    public byte[] getAdsImage(Long id) {
        return getImageFromDB(id).getData();
    }

    private Image getImageFromDB(long id) {
        return imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
    }

    private void extractInfoFromFile(MultipartFile file, Image imageToSave) {
        if (file.isEmpty()) {
            log.warn("File '{}' is empty!", file.getOriginalFilename());
            throw new EmptyFileException();
        }
        byte[] imageData;
        try {
            imageData = file.getBytes();
        } catch (IOException e) {
            log.error("File '{}' has some problems and cannot be read.", file.getOriginalFilename());
            throw new ImageCanNotBeReadException("Problems with uploaded image");
        }
        imageToSave.setData(imageData);
        imageToSave.setFileSize(file.getSize());
        imageToSave.setMediaType(file.getContentType());
    }
}
