package com.example.basicarch.module.code.facade;

import com.example.basicarch.base.annotation.Facade;
import com.example.basicarch.base.cache.RedisCacheEventService;
import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.exception.SystemErrorCode;
import com.example.basicarch.base.exception.ToyAssert;
import com.example.basicarch.module.code.converter.CodeConverter;
import com.example.basicarch.module.code.converter.CodeGroupConverter;
import com.example.basicarch.module.code.model.CodeGroupModel;
import com.example.basicarch.module.code.model.CodeModel;
import com.example.basicarch.module.code.service.CodeGroupService;
import com.example.basicarch.module.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class CodeFacade {
    private final CodeGroupService codeGroupService;
    private final CodeService codeService;
    private final CodeConverter codeConverter;
    private final CodeGroupConverter codeGroupConverter;
    private final RedisCacheEventService redisCacheEventService;

    public void createCodeGroup(CodeGroupModel codeGroupModel) {
        codeGroupService.save(codeGroupConverter.toEntity(codeGroupModel));
        redisCacheEventService.publishEvent(CacheType.CODE);
    }

    public void updateCodeGroup(CodeGroupModel codeGroupModel) {
        codeGroupService.update(codeGroupConverter.toEntity(codeGroupModel));
        redisCacheEventService.publishEvent(CacheType.CODE);
    }

    @Transactional
    public void removeCodeGroupById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID를 입력해주세요.");
        codeService.deleteByCodeGroupId(id);
        codeGroupService.deleteById(id);
        redisCacheEventService.publishEvent(CacheType.CODE);
    }

    public void createCode(CodeModel codeModel) {
        ToyAssert.notNull(codeModel.getCodeGroupId(), SystemErrorCode.REQUIRED, "code_group_id가 입력되지 않았습니다.");
        codeService.save(codeConverter.toEntity(codeModel));
        redisCacheEventService.publishEvent(CacheType.CODE);
    }

    public void updateCode(CodeModel codeModel) {
        codeService.update(codeConverter.toEntity(codeModel));
        redisCacheEventService.publishEvent(CacheType.CODE);
    }

    public void removeCodeById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID를 입력해주세요.");
        codeService.deleteById(id);
        redisCacheEventService.publishEvent(CacheType.CODE);
    }
}
