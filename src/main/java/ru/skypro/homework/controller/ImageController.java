package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping
public class ImageController {

    private  final ImageService imageService;

    @PatchMapping (value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateAdsImage(@PathVariable Long id, @RequestParam MultipartFile image){
        log.info("Was invoked updateAdsImage method from {}", ImageController.class.getSimpleName());
        try {
            byte[] imageBytes = imageService.updateAdsImage(id, image);
            return ResponseEntity.ok(imageBytes);
        } catch (IOException e) {
            log.error("Image has some problems and cannot be read");
            throw new RuntimeException("Problems with uploaded image");
        }
    }
}
