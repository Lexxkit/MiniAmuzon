package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.exceptions.AdsNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdsServiceImpl implements AdsService {
    private final AdsRepository adsRepository;
    private final AdsMapper adsMapper;
    @Override
    public ResponseWrapperAds findAllAds() {
        log.info("Was invoked findAllAds method from {}", AdsService.class.getSimpleName());
        List<Ads> adsList = adsRepository.findAll();
        return adsMapper.adsListToResponseWrapperAds(adsList.size(), adsList);
    }

    @Override
    public AdsDto createAds(CreateAdsDto createAdsDto, MultipartFile image) {
        log.info("Was invoked createAds method from {}", AdsService.class.getSimpleName());
        Ads ads = adsMapper.createAdsDtoToAds(createAdsDto);
        // Добавить обработку изображения и добавление его в список к рекламе!
        Ads savedAds = adsRepository.save(ads);
        return adsMapper.adsToAdsDto(savedAds);
    }

    @Override
    public FullAdsDto findFullAdsById(long id) {
        Ads ads = getAdsById(id);
        return adsMapper.adsToFullAdsDto(ads);
    }

    @Override
    public void removeAds(long id) {
        Ads ads = getAdsById(id);
        adsRepository.delete(ads);
    }

    @Override
    public AdsDto updateAdsById(long id, CreateAdsDto createAdsDto) {
        Ads oldAds = getAdsById(id);
        Ads infoToUpdate = adsMapper.createAdsDtoToAds(createAdsDto);

        oldAds.setPrice(infoToUpdate.getPrice());
        oldAds.setTitle(infoToUpdate.getTitle());
        oldAds.setDescription(infoToUpdate.getDescription());

        Ads updatedAds = adsRepository.save(oldAds);
        return adsMapper.adsToAdsDto(updatedAds);
    }

    @Override
    public ResponseWrapperAds findAllAdsForUser(String username) {
        // TODO: 18.01.2023 Refactor with UserRepository - find user by email (thr exception), then user.getAdsList()
        List<Ads> userAdsList = adsRepository.findAdsByAuthorEmail(username);
        return adsMapper.adsListToResponseWrapperAds(userAdsList.size(), userAdsList);
    }

    private Ads getAdsById(long id) {
        return adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
    }
}