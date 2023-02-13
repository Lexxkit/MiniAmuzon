package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exceptions.AdsNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
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
    private final ImageService imageService;
    private final UserService userService;

    /**
     * Receive all Ads.
     * The repository method {@link AdsRepository#findAll()} is used to get a List of all ads.
     *
     * @return {@link ResponseWrapperAds} instance with number of founded ads and List of {@link AdsDto}
     */
    @Override
    public ResponseWrapperAds getAllAds() {
        log.info("Was invoked findAllAds method from {}", AdsService.class.getSimpleName());
        List<Ads> adsList = adsRepository.findAll();
        return adsMapper.adsListToResponseWrapperAds(adsList.size(), adsList);
    }

    /**
     * Creates new ad.
     *
     * @param createAdsDto {@link CreateAdsDto} from a client
     * @param image {@link MultipartFile} with image from a client
     * @param authentication {@link Authentication} instance from controller
     * @return {@link AdsDto} instance of the created ad
     */
    @Override
    @Transactional
    public AdsDto createAds(CreateAdsDto createAdsDto, MultipartFile image, Authentication authentication) {
        log.info("Was invoked createAds method from {}", AdsService.class.getSimpleName());
        User currentUser = userService.getUser(authentication.getName());
        Ads ads = adsMapper.createAdsDtoToAds(createAdsDto);
        ads.setAuthor(currentUser);
        Ads savedAds = adsRepository.save(ads);

        Image adsImage = imageService.createImage(image, savedAds);
        savedAds.setImages(List.of(adsImage));
        return adsMapper.adsToAdsDto(savedAds);
    }

    /**
     * Search for a full ad in DB by ID.
     *
     * @param id identification number of ad
     * @return {@link FullAdsDto} instance for desired ad
     */
    @Override
    public FullAdsDto getFullAdsById(long id) {
        log.info("Was invoked getFullAdsById method from {}", AdsService.class.getSimpleName());
        Ads ads = getAdsById(id);
        return adsMapper.adsToFullAdsDto(ads);
    }

    /**
     * Delete ad from DB by id.
     * The repository method {@link AdsRepository#delete(Object)} is used.
     *
     * @param id identification number of ad
     * @param authentication {@link Authentication} instance from controller
     */
    @Override
    public void removeAds(long id, Authentication authentication) {
        log.info("Was invoked removeAds method from {}", AdsService.class.getSimpleName());
        Ads ads = getAdsById(id);

        userService.checkIfUserHasPermissionToAlter(authentication, ads.getAuthor().getUsername());
        adsRepository.delete(ads);
    }

    /**
     * Update an ad by its ID.
     *
     * @param id identification number of ad
     * @param createAdsDto {@link CreateAdsDto} instance from a client
     * @param authentication {@link Authentication} instance from controller
     * @return {@link AdsDto} instance with updated information
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
     * Receive all ads for the particular user.
     *
     * @param username name of a user
     * @return {@link ResponseWrapperAds} instance with number of founded ads and List of {@link AdsDto}
     */
    @Override
    public ResponseWrapperAds getAllAdsForUser(String username) {
        log.info("Was invoked getAllAdsForUser method from {}", AdsService.class.getSimpleName());
        List<Ads> userAdsList = adsRepository.findAdsByAuthorUsername(username);
        return adsMapper.adsListToResponseWrapperAds(userAdsList.size(), userAdsList);
    }

    /**
     * Receive ad by id.
     * The repository method {@link AdsRepository#findById(Object)} is used.
     * @param id identification number of ad
     * @return {@link Ads} instance
     * @throws AdsNotFoundException if no ad was found
     */
    @Override
    public Ads getAdsById(long id) {
        log.info("Was invoked getAdsById method from {}", AdsService.class.getSimpleName());
        return adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
    }
}
