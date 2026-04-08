package com.basicarch.module.code.converter;

import com.basicarch.base.converter.TypeConverter;
import com.basicarch.module.code.model.CodeGroupModel;
import com.basicarch.module.code.entity.CodeGroup;
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
        , uses = {TypeConverter.class, CodeConverter.class}
)
public interface CodeGroupConverter {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "codeList", target = "codeModelList")
    @Mapping(source = "createId", target = "createId")
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "updateId", target = "updateId")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "localDateTimeToString")
    CodeGroupModel toModel(CodeGroup codeGroup);

    @Mapping(target = "rowNum", ignore = true)
    @Mapping(source = "codeModelList", target = "codeList")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createId", target = "createId")
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "updateId", target = "updateId")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "stringToLocalDateTime")
    CodeGroup toEntity(CodeGroupModel codeGroupModel);

    List<CodeGroupModel> toModelList(List<CodeGroup> codeGroupList);

    List<CodeGroup> toEntityList(List<CodeGroupModel> codeGroupModelList);
}
