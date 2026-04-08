package com.basicarch.module.code.converter;

import com.basicarch.base.converter.TypeConverter;
import com.basicarch.base.model.pager.PageInfo;
import com.basicarch.base.model.pager.PageResponse;
import com.basicarch.module.code.model.CodeModel;
import com.basicarch.module.code.entity.Code;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

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
public interface CodeConverter {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createId", target = "createId")
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "updateId", target = "updateId")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "localDateTimeToString")
    CodeModel toModel(Code code);

    @Mapping(target = "rowNum", ignore = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createId", target = "createId")
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "updateId", target = "updateId")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "stringToLocalDateTime")
    Code toEntity(CodeModel codeModel);

    List<CodeModel> toModelList(List<Code> codeList);

    List<Code> toEntityList(List<CodeModel> codeModelList);

    default PageResponse<CodeModel> toPageResponse(Page<Code> page) {
        PageInfo pageInfo = PageInfo.from(page);
        List<CodeModel> list = toModelList(page.getContent());
        return new PageResponse<>(pageInfo, list);
    }
}
