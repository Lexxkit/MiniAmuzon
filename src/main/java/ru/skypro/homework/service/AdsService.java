package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;

public interface AdsService {
    ResponseWrapperAds findAllAds();

    AdsDto createAds(CreateAdsDto createAdsDto, MultipartFile image);

    FullAdsDto findFullAdsById(long id);

    void removeAds(long id);

    AdsDto updateAdsById(long id, CreateAdsDto createAdsDto);

    ResponseWrapperAds findAllAdsForUser(String username);
}
