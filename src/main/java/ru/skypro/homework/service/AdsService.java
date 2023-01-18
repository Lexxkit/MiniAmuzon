package ru.skypro.homework.service;

import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;

public interface AdsService {
    ResponseWrapperAds findAllAds();

    AdsDto createAds(CreateAdsDto createAdsDto);

    FullAdsDto findFullAdsById(long id);
}
