package com.example.basicarch.module.code.service;

import com.example.basicarch.module.code.entity.Code;
import com.example.basicarch.module.code.model.CodeSearchParam;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeCacheService {
    private final CodeService codeService;

    @Cacheable(value = "code", key = "'all'")
    public List<Code> findAll() {
        return codeService.findAllBy(new CodeSearchParam());
    }

    @CacheEvict(value = "code", allEntries = true)
    public void evict() {}
}
