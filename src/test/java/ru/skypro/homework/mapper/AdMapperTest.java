package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.User;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AdMapperTest {

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
}
