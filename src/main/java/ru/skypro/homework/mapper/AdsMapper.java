package ru.skypro.homework.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdsMapper {
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    AdsDto adsToAdsDto(Ads ads);

    @Mapping(source = "author", target = "author.id")
    @Mapping(source = "pk", target = "id")
    Ads adsDtoToAds(AdsDto adsDto);

    @Mapping(source = "size", target = "count")
    @Mapping(source = "adsList", target = "results")
    ResponseWrapperAds adsListToResponseWrapperAds(Integer size, List<Ads> adsList);

    Ads createAdsDtoToAds(CreateAdsDto createAdsDto);

    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "id", target = "pk")
    FullAdsDto adsToFullAdsDto(Ads ads);
}
