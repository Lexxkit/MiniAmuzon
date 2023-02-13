package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.ResponseWrapperUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(source = "avatar", target = "image", qualifiedByName = "getUserAvatarLink")
    UserDto userToUserDto(User user);

    @Mapping(source = "email", target = "username")
    User userDtoToUser(UserDto userDto);

    @Mapping(source = "size", target = "count")
    @Mapping(source = "userList", target = "results")
    ResponseWrapperUserDto userListToResponseWrapperUserDto(Integer size,List<User> userList);

    @Named("getUserAvatarLink")
    default String getUserAvatarLink(Avatar avatar) {
        return (avatar == null) ? null : "/users/avatar/" + avatar.getId();
    }

}
