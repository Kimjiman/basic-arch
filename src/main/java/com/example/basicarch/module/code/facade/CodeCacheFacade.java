package com.example.basicarch.module.code.facade;

import com.example.basicarch.base.annotation.Facade;
import com.example.basicarch.base.cache.RedisCacheEventService;
import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.redis.CacheEventHandler;
import com.example.basicarch.base.utils.JsonUtils;
import com.example.basicarch.module.code.converter.CodeConverter;
import com.example.basicarch.module.code.converter.CodeGroupConverter;
import com.example.basicarch.module.code.entity.Code;
import com.example.basicarch.module.code.model.CodeGroupModel;
import com.example.basicarch.module.code.model.CodeGroupSearchParam;
import com.example.basicarch.module.code.model.CodeModel;
import com.example.basicarch.module.code.model.CodeSearchParam;
import com.example.basicarch.module.code.service.CodeGroupService;
import com.example.basicarch.module.code.service.CodeService;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Facade
@Slf4j
@RequiredArgsConstructor
public class CodeCacheFacade implements CacheEventHandler {
    private static final String CACHE_KEY = "cache:code";
    private static final String CACHE_FIELD_ALL = "all";

    private final CodeGroupService codeGroupService;
    private final CodeService codeService;
    private final CodeConverter codeConverter;
    private final CodeGroupConverter codeGroupConverter;
    private final RedisCacheEventService redisCacheEventService;

    @Override
    public CacheType getSupportedCacheType() {
        return CacheType.CODE;
    }

    @Override
    public void handle() {
        refresh();
    }

    @PostConstruct
    public void init() {
        try {
            refresh();
        } catch (Exception e) {
            log.warn("[CodeCacheFacade] refresh Error: {}", e.getMessage());
        }
    }

    public void refresh() {
        List<Code> codes = codeService.findAllBy(new CodeSearchParam());
        redisCacheEventService.save(CACHE_KEY, CACHE_FIELD_ALL, JsonUtils.toJson(codes));
        log.info("Code cache refreshed. size: {}", codes.size());
    }

    private List<Code> cachedEntities() {
        return redisCacheEventService.get(CACHE_KEY, CACHE_FIELD_ALL)
                .map(it -> JsonUtils.<List<Code>>fromJson(it, new TypeToken<List<Code>>() {}.getType()))
                .orElseGet(() -> codeService.findAllBy(new CodeSearchParam()));
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
        return codeConverter.toModelList(cachedEntities());
    }

    public CodeModel findCodeById(Long id) {
        if (id == null) return null;
        return cachedEntities().stream()
                .filter(it -> id.equals(it.getId()))
                .map(codeConverter::toModel)
                .findFirst()
                .orElse(null);
    }
}
