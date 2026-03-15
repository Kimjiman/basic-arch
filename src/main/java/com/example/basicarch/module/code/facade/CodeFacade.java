package com.example.basicarch.module.code.facade;

import com.example.basicarch.base.annotation.CacheInvalidate;
import com.example.basicarch.base.annotation.Facade;
import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.exception.SystemErrorCode;
import com.example.basicarch.base.exception.ToyAssert;
import com.example.basicarch.module.code.converter.CodeConverter;
import com.example.basicarch.module.code.converter.CodeGroupConverter;
import com.example.basicarch.module.code.model.CodeGroupModel;
import com.example.basicarch.module.code.model.CodeGroupSearchParam;
import com.example.basicarch.module.code.model.CodeModel;
import com.example.basicarch.module.code.model.CodeSearchParam;
import com.example.basicarch.module.code.service.CodeGroupCacheService;
import com.example.basicarch.module.code.service.CodeGroupService;
import com.example.basicarch.module.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Facade
@RequiredArgsConstructor
public class CodeFacade {
    private final CodeGroupService codeGroupService;
    private final CodeService codeService;
    private final CodeGroupCacheService codeGroupCacheService;
    private final CodeConverter codeConverter;
    private final CodeGroupConverter codeGroupConverter;

    public void refresh() {
        codeGroupCacheService.evict();
        codeGroupCacheService.findAll(new CodeGroupSearchParam());
        log.info("[CodeFacade] code cache refreshed.");
    }

    public List<CodeGroupModel> findCodeGroupAllBy(CodeGroupSearchParam param) {
        return codeGroupConverter.toModelList(codeGroupCacheService.findAll(param));
    }

    public CodeGroupModel findCodeGroupById(Long id) {
        return codeGroupService.findByIdWithCodes(id)
                .map(codeGroupConverter::toModel)
                .orElse(null);
    }

    public List<CodeModel> findCodeAllBy(CodeSearchParam param) {
        return codeConverter.toModelList(
                codeGroupCacheService.findAll(new CodeGroupSearchParam()).stream()
                        .flatMap(it -> it.getCodeList().stream())
                        .filter(it -> param.getCodeGroupId() == null || param.getCodeGroupId().equals(it.getCodeGroupId()))
                        .filter(it -> param.getName() == null || it.getName().contains(param.getName()))
                        .toList()
        );
    }

    public CodeModel findCodeById(Long id) {
        if (id == null) return null;
        return codeGroupCacheService.findAll(new CodeGroupSearchParam()).stream()
                .flatMap(it -> it.getCodeList().stream())
                .filter(it -> id.equals(it.getId()))
                .findFirst()
                .map(codeConverter::toModel)
                .orElse(null);
    }

    @CacheInvalidate(CacheType.CODE)
    public void createCodeGroup(CodeGroupModel codeGroupModel) {
        ToyAssert.notNull(codeGroupModel.getName(), SystemErrorCode.REQUIRED, "name이 입력되지 않았습니다.");
        ToyAssert.notNull(codeGroupModel.getCodeGroup(), SystemErrorCode.REQUIRED, "codeGroup이 입력되지 않았습니다.");

        codeGroupService.save(codeGroupConverter.toEntity(codeGroupModel));
    }

    @CacheInvalidate(CacheType.CODE)
    public void updateCodeGroup(CodeGroupModel codeGroupModel) {
        ToyAssert.notNull(codeGroupModel.getId(), SystemErrorCode.REQUIRED, "ID이 입력되지 않았습니다.");
        ToyAssert.notNull(codeGroupModel.getName(), SystemErrorCode.REQUIRED, "name이 입력되지 않았습니다.");
        ToyAssert.notNull(codeGroupModel.getCodeGroup(), SystemErrorCode.REQUIRED, "codeGroup이 입력되지 않았습니다.");

        codeGroupService.update(codeGroupConverter.toEntity(codeGroupModel));
    }

    @CacheInvalidate(CacheType.CODE)
    @Transactional
    public void removeCodeGroupById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID를 입력해주세요.");
        codeService.deleteByCodeGroupId(id);
        codeGroupService.deleteById(id);
    }

    @CacheInvalidate(CacheType.CODE)
    public void createCode(CodeModel codeModel) {
        ToyAssert.notNull(codeModel.getCodeGroupId(), SystemErrorCode.REQUIRED, "codeGroupId가 입력되지 않았습니다.");
        ToyAssert.notNull(codeModel.getCode(), SystemErrorCode.REQUIRED, "code가 입력되지 않았습니다.");
        ToyAssert.notNull(codeModel.getName(), SystemErrorCode.REQUIRED, "name이 입력되지 않았습니다.");
        codeService.save(codeConverter.toEntity(codeModel));
    }

    @CacheInvalidate(CacheType.CODE)
    public void updateCode(CodeModel codeModel) {
        ToyAssert.notNull(codeModel.getCodeGroupId(), SystemErrorCode.REQUIRED, "codeGroupId가 입력되지 않았습니다.");
        ToyAssert.notNull(codeModel.getId(), SystemErrorCode.REQUIRED, "id가 입력되지 않았습니다.");
        ToyAssert.notNull(codeModel.getCode(), SystemErrorCode.REQUIRED, "code가 입력되지 않았습니다.");
        ToyAssert.notNull(codeModel.getName(), SystemErrorCode.REQUIRED, "name이 입력되지 않았습니다.");
        codeService.update(codeConverter.toEntity(codeModel));
    }

    @CacheInvalidate(CacheType.CODE)
    public void removeCodeById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID이 입력되지 않았습니다.");
        codeService.deleteById(id);
    }
}
