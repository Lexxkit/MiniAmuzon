package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.User;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AdsMapperTest {

    private final AdsMapper adsMapper = Mappers.getMapper(AdsMapper.class);
    @Test
    public void shouldMapAdsToAdsDto() {
        //given
        User testUser = new User();
        testUser.setId(42L);
        Ads ads = new Ads();
        ads.setId(1L);
        ads.setPrice(new BigDecimal(10));
        ads.setTitle("Test ads");
        ads.setAuthor(testUser);

        //when
        AdsDto adsDto = adsMapper.adsToAdsDto(ads);
        System.out.println(adsDto);

        //then
        assertThat(adsDto).isNotNull();
        assertThat(adsDto.getAuthor()).isEqualTo(testUser.getId().intValue());
        assertThat(adsDto.getImage()).isNull();
        assertThat(adsDto.getPk()).isEqualTo(ads.getId().intValue());
        assertThat(adsDto.getPrice()).isEqualTo(ads.getPrice().intValue());
        assertThat(adsDto.getTitle()).isEqualTo(ads.getTitle());
    }

    @Test
    public void shouldMapAdsDtoToAds() {
        //given
        AdsDto adsDto = new AdsDto();
        adsDto.setAuthor(42);
        adsDto.setImage(null);
        adsDto.setPk(1);
        adsDto.setPrice(10);
        adsDto.setTitle("Test dto");

        //when
        Ads ads = adsMapper.adsDtoToAds(adsDto);
        System.out.println(ads);

        //then
        assertThat(ads).isNotNull();
        assertThat(ads.getId().intValue()).isEqualTo(adsDto.getPk());
        assertThat(ads.getAuthor().getId().intValue()).isEqualTo(adsDto.getAuthor());
        assertThat(ads.getPrice().intValue()).isEqualTo(adsDto.getPrice());
        assertThat(ads.getTitle()).isEqualTo(adsDto.getTitle());
        assertThat(ads.getImages()).isNull();
        assertThat(ads.getComments()).isNull();
    }

    @Test
    public void shouldMapAdsListToResponseWrapperAds() {
        //given
        User testUser = new User();
        testUser.setId(42L);
        Ads ads1 = new Ads();
        ads1.setId(1L);
        ads1.setPrice(new BigDecimal(10));
        ads1.setTitle("Test ads");
        ads1.setAuthor(testUser);
        Ads ads2 = new Ads();
        ads2.setId(2L);
        ads2.setPrice(new BigDecimal(20));
        ads2.setTitle("Test ads 2");
        ads2.setAuthor(testUser);
        List<Ads> adsList = List.of(ads1, ads2);

        //when
        ResponseWrapperAds rwa = adsMapper.adsListToResponseWrapperAds(adsList.size(), adsList);
        System.out.println(rwa);

        //then
        assertThat(rwa).isNotNull();
        assertThat(rwa.getCount()).isEqualTo(adsList.size());
        assertThat(rwa.getResults()).isNotNull();
        assertThat(rwa.getResults().get(0)).isEqualTo(adsMapper.adsToAdsDto(ads1));
    }

    @Test
    public void shouldMapCreateAdsDtoToAds() {
        //given
        CreateAdsDto createAdsDto = new CreateAdsDto();
        createAdsDto.setDescription("Test description");
        createAdsDto.setPrice(10);
        createAdsDto.setTitle("Test dto");

        //when
        Ads ads = adsMapper.createAdsDtoToAds(createAdsDto);
        System.out.println(ads);

        //then
        assertThat(ads).isNotNull();
        assertThat(ads.getId()).isNull();
        assertThat(ads.getAuthor()).isNull();
        assertThat(ads.getPrice().intValue()).isEqualTo(createAdsDto.getPrice());
        assertThat(ads.getTitle()).isEqualTo(createAdsDto.getTitle());
        assertThat(ads.getDescription()).isEqualTo(createAdsDto.getDescription());
        assertThat(ads.getImages()).isNull();
        assertThat(ads.getComments()).isNull();
    }

    @Test
    public void shouldMapAdsToFullAdsDto() {
        //given
        User testUser = new User();
        testUser.setId(42L);
        Ads ads = new Ads();
        ads.setId(1L);
        ads.setPrice(new BigDecimal(10));
        ads.setTitle("Test ads");
        ads.setAuthor(testUser);

        //when
        FullAdsDto fullAdsDto = adsMapper.adsToFullAdsDto(ads);
        System.out.println(fullAdsDto);

        //then
        assertThat(fullAdsDto).isNotNull();
        assertThat(fullAdsDto.getAuthorFirstName()).isNull();
        assertThat(fullAdsDto.getPk()).isEqualTo(ads.getId().intValue());
        assertThat(fullAdsDto.getPrice()).isEqualTo(ads.getPrice().intValue());
        assertThat(fullAdsDto.getTitle()).isEqualTo(ads.getTitle());
    }
}
