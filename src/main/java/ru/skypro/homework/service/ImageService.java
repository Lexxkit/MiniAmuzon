package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
 byte[] updateAdsImage(Long id, MultipartFile image);


}
