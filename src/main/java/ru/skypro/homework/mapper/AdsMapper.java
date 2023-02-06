package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdsMapper {
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "ads", target = "image", qualifiedByName = "getImageLink")
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
    @Mapping(source = "author.username", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "ads", target = "image", qualifiedByName = "getImageLink")
    FullAdsDto adsToFullAdsDto(Ads ads);

    @Named("getImageLink")
    default String getImageLink(Ads ads) {
        Image adsImage = ads.getRandomAdsImage();
        return (adsImage == null) ? null : "/image/" + adsImage.getId().toString();
    }
}
