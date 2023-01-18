package ru.skypro.homework.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
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
}
