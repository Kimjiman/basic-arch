package com.example.basicarch.module.code.service;

import com.example.basicarch.base.constants.CacheType;
import com.example.basicarch.base.service.BaseCacheService;
import com.example.basicarch.module.code.entity.Code;
import com.example.basicarch.module.code.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeCacheService implements BaseCacheService {
    private final CodeRepository codeRepository;

    @Override
    public CacheType getCacheType() {
        return CacheType.CODE;
    }

    @Override
    @CacheEvict(value = CacheType.Names.CODE, allEntries = true)
    public void evict() {}

    @Cacheable(value = CacheType.Names.CODE, key = "'all'")
    public List<Code> findAll() {
        return codeRepository.findAll();
    }
}
