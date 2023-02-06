package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exceptions.AdsNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdsServiceImpl implements AdsService {
    private final AdsRepository adsRepository;
    private final AdsMapper adsMapper;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final UserService userService;

    /**
     * Receive all Ads
     * The Service method is being used{@link AdsService#getAllAds()}
     * wherein the repository method is used to get all declarations{@link AdsRepository#findAll()}
     *
     * @return ad list
     */
    @Override
    public ResponseWrapperAds getAllAds() {
        log.info("Was invoked findAllAds method from {}", AdsService.class.getSimpleName());
        List<Ads> adsList = adsRepository.findAll();
        return adsMapper.adsListToResponseWrapperAds(adsList.size(), adsList);
    }

    /**
     * Creating of new ad
     *
     * @param createAdsDto
     * @param image
     * @param authentication
     * @return ad created
     */
    @Override
    @Transactional
    public AdsDto createAds(CreateAdsDto createAdsDto, MultipartFile image, Authentication authentication) {
        log.info("Was invoked createAds method from {}", AdsService.class.getSimpleName());
        UserDto currentUserDto = userService.getUserDtoByUsername(authentication.getName());
        Ads ads = adsMapper.createAdsDtoToAds(createAdsDto);
        ads.setAuthor(userMapper.userDtoToUser(currentUserDto));
        Ads savedAds = adsRepository.save(ads);

        Image adsImage = imageService.createImage(image, savedAds);
        savedAds.setImages(List.of(adsImage));
        return adsMapper.adsToAdsDto(savedAds);
    }

    /**
     * Search for a full ad in DB by ID
     *
     * @param id
     * @return
     */
    @Override
    public FullAdsDto getFullAdsById(long id) {
        log.info("Was invoked getFullAdsById method from {}", AdsService.class.getSimpleName());
        Ads ads = getAdsById(id);
        return adsMapper.adsToFullAdsDto(ads);
    }

    /**
     * Delete ad from DB by id
     * The repository method is being used {@link AdsRepository#delete(Object)}
     *
     * @param id
     * @param authentication
     */
    @Override
    public void removeAds(long id, Authentication authentication) {
        log.info("Was invoked removeAds method from {}", AdsService.class.getSimpleName());
        Ads ads = getAdsById(id);

        userService.checkIfUserHasPermissionToAlter(authentication, ads.getAuthor().getUsername());
        adsRepository.delete(ads);
    }

    /**
     * Receive old ad by id, update and save
     *
     * @param id
     * @param createAdsDto
     * @param authentication
     * @return ad update
     */
    @Override
    public AdsDto updateAdsById(long id, CreateAdsDto createAdsDto, Authentication authentication) {
        log.info("Was invoked updateAdsById method from {}", AdsService.class.getSimpleName());
        Ads oldAds = getAdsById(id);
        userService.checkIfUserHasPermissionToAlter(authentication, oldAds.getAuthor().getUsername());
        Ads infoToUpdate = adsMapper.createAdsDtoToAds(createAdsDto);

        oldAds.setPrice(infoToUpdate.getPrice());
        oldAds.setTitle(infoToUpdate.getTitle());
        oldAds.setDescription(infoToUpdate.getDescription());

        Ads updatedAds = adsRepository.save(oldAds);
        return adsMapper.adsToAdsDto(updatedAds);
    }

    /**
     * Receive all ads for user
     *
     * @param username
     * @return ad list
     */
    @Override
    public ResponseWrapperAds getAllAdsForUser(String username) {
        log.info("Was invoked getAllAdsForUser method from {}", AdsService.class.getSimpleName());
        List<Ads> userAdsList = adsRepository.findAdsByAuthorUsername(username);
        return adsMapper.adsListToResponseWrapperAds(userAdsList.size(), userAdsList);
    }

    /**
     * Receive ad by id
     * The repository method is being used {@link AdsRepository#findById(Object)}
     * @param id
     * @return ad by id
     * @throws AdsNotFoundException if no ad was found
     */
    @Override
    public Ads getAdsById(long id) {
        log.info("Was invoked getAdsById method from {}", AdsService.class.getSimpleName());
        return adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
    }
}
