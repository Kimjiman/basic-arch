package com.example.basicarch.module.code.facade;

import com.example.basicarch.base.annotation.Facade;
import com.example.basicarch.module.code.converter.CodeConverter;
import com.example.basicarch.module.code.converter.CodeGroupConverter;
import com.example.basicarch.module.code.model.CodeGroupModel;
import com.example.basicarch.module.code.model.CodeGroupSearchParam;
import com.example.basicarch.module.code.model.CodeModel;
import com.example.basicarch.module.code.model.CodeSearchParam;
import com.example.basicarch.module.code.service.CodeCacheService;
import com.example.basicarch.module.code.service.CodeGroupService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Facade
@Slf4j
@RequiredArgsConstructor
public class CodeCacheFacade {
    private final CodeGroupService codeGroupService;
    private final CodeCacheService codeCacheService;
    private final CodeConverter codeConverter;
    private final CodeGroupConverter codeGroupConverter;

    @PostConstruct
    public void init() {
        try {
            refresh();
        } catch (Exception e) {
            log.warn("[CodeCacheFacade] refresh Error: {}", e.getMessage());
        }
    }

    public void refresh() {
        codeCacheService.evict();
        codeCacheService.findAll();
        log.info("Code cache refreshed.");
    }

    public List<CodeGroupModel> findCodeGroupAllBy(CodeGroupSearchParam param) {
        return codeGroupConverter.toModelList(codeGroupService.findAllBy(param));
    }

    public CodeGroupModel findCodeGroupById(Long id) {
        return codeGroupService.findByIdWithCodes(id)
                .map(codeGroupConverter::toModel)
                .orElse(null);
    }

    public List<CodeModel> findCodeAllBy(CodeSearchParam param) {
        return codeConverter.toModelList(codeCacheService.findAll());
    }

    public CodeModel findCodeById(Long id) {
        if (id == null) return null;
        return codeCacheService.findAll().stream()
                .filter(it -> id.equals(it.getId()))
                .map(codeConverter::toModel)
                .findFirst()
                .orElse(null);
    }
}
