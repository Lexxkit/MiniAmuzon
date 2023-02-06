package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exceptions.AdsNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.mapper.AdsMapperImpl;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.impl.AdsServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdsServiceImplTest {
    @Mock
    private AdsRepository adsRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;
    @Spy
    private AdsMapper adsMapper = new AdsMapperImpl();
    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    @InjectMocks
    private AdsServiceImpl out;

    private List<Ads> adsList;
    private Ads ads1;
    private Ads ads2;
    private CreateAdsDto createAdsDto;
    private User testUser;
    private Authentication auth;
    private Image testImage;

    @BeforeEach
    void init() {
        testUser = new User();
        testUser.setId(42L);
        testUser.setUsername("test@test.com");
        auth = new UsernamePasswordAuthenticationToken(testUser, null);

        testImage = new Image();
        testImage.setId(1L);

        createAdsDto = new CreateAdsDto();
        createAdsDto.setDescription("Test description");
        createAdsDto.setTitle("Test title");
        createAdsDto.setPrice(15);

        ads1 = new Ads();
        ads1.setId(1L);
        ads1.setPrice(new BigDecimal(10));
        ads1.setTitle("Test ads");
        ads1.setAuthor(testUser);

        ads2 = new Ads();
        ads2.setId(2L);
        ads2.setPrice(new BigDecimal(20));
        ads2.setTitle("Test ads 2");
        ads2.setAuthor(testUser);

        adsList = List.of(ads1, ads2);
    }

    @Test
    void shouldReturnResponseWrapperAdsWithAllAds_whenGetAllAds() {
        when(adsRepository.findAll()).thenReturn(adsList);
        ResponseWrapperAds result = out.getAllAds();
        System.out.println(result);

        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(adsList.size());
        assertThat(result.getResults()).isNotNull();
    }

    @Test
    void shouldReturnAdsDto_WhenCreateAds() {
        Ads adsForMockSave  = adsMapper.createAdsDtoToAds(createAdsDto);
        when(imageService.createImage(any(), any())).thenReturn(testImage);
        when(adsRepository.save(any(Ads.class))).thenReturn(adsForMockSave);
        when(userService.getUserDtoByUsername(any(String.class))).thenReturn(null);
        AdsDto result = out.createAds(createAdsDto, null, auth);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(createAdsDto.getTitle());
        assertThat(result.getPrice()).isEqualTo(createAdsDto.getPrice());
    }

    @Test
    void shouldThrowException_whenGetAdsWithWrongId() {
        when(adsRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> out.getAdsById(1L)).isInstanceOf(AdsNotFoundException.class);
    }

    @Test
    void shouldReturnFullAdsDto_whenGetFullAdsById() {
        when(adsRepository.findById(any(Long.class))).thenReturn(Optional.of(ads1));
        FullAdsDto result = out.getFullAdsById(ads1.getId());

        assertThat(result).isNotNull();
        assertThat(result.getPk()).isEqualTo(ads1.getId().intValue());
        assertThat(result.getTitle()).isEqualTo(ads1.getTitle());
        assertThat(result.getPrice()).isEqualTo(ads1.getPrice().intValue());
        assertThat(result.getAuthorFirstName()).isNull();
    }

    @Test
    void shouldReturnResponseWrapperAdsForUser_whenGetAllAdsForUser() {
        when(adsRepository.findAdsByAuthorUsername(any(String.class))).thenReturn(adsList);
        ResponseWrapperAds result = out.getAllAdsForUser("Username");

        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(adsList.size());
        assertThat(result.getResults().contains(adsMapper.adsToAdsDto(ads1))).isTrue();
    }
}