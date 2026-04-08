package com.basicarch.module.code.service;

import com.basicarch.base.constants.CacheType;
import com.basicarch.base.service.BaseCacheService;
import com.basicarch.module.code.entity.CodeGroup;
import com.basicarch.module.code.model.CodeGroupSearchParam;
import com.basicarch.module.code.repository.CodeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeGroupCacheService implements BaseCacheService {
    private final CodeGroupRepository codeGroupRepository;

    @Override
    public CacheType getCacheType() {
        return CacheType.CODE;
    }

    @Override
    @CacheEvict(value = CacheType.Names.CODE, allEntries = true)
    public void evict() {}

    @Cacheable(value = CacheType.Names.CODE, key = "'all'", unless = "#result == null || #result.isEmpty()")
    public List<CodeGroup> findAll(CodeGroupSearchParam param) {
        return codeGroupRepository.findAllBy(param);
    }
}
