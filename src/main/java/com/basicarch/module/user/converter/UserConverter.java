package com.basicarch.module.user.converter;

import com.basicarch.base.converter.TypeConverter;
import com.basicarch.module.user.model.UserModel;
import com.basicarch.module.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * packageName    : com.basicarch.module.code.converter
 * fileName       : CodeMapper
 * author         : KIM JIMAN
 * date           : 25. 8. 11. 월요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 25. 8. 11.     KIM JIMAN      First Commit
 */
@Mapper(
        componentModel = "spring"
        , uses = {TypeConverter.class}
)
public interface UserConverter {
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "useYn", target = "useYn", qualifiedByName = "stringToYn")
    UserModel toModel(User user);

    @Mapping(target = "rowNum", ignore = true)
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "useYn", target = "useYn", qualifiedByName = "ynToString")
    User toEntity(UserModel userModel);

    List<UserModel> toModelList(List<User> userList);

    List<User> toEntityList(List<UserModel> userModelList);
}
