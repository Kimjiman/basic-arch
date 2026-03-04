package com.example.basicarch.module.code.service;

import com.example.basicarch.base.service.BaseService;
import com.example.basicarch.module.code.entity.Code;
import com.example.basicarch.module.code.model.CodeSearchParam;
import com.example.basicarch.module.code.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CodeService implements BaseService<Code, CodeSearchParam, Long> {
    private final CodeRepository codeRepository;

    @Override
    public boolean existsById(Long id) {
        return codeRepository.existsById(id);
    }

    @Override
    public Optional<Code> findById(Long id) {
        return codeRepository.findById(id);
    }

    @Override
    public List<Code> findAllBy(CodeSearchParam param) {
        return codeRepository.findAllBy(param);
    }

    public Page<Code> findAllBy(CodeSearchParam param, Pageable pageable) {
        return codeRepository.findAllBy(param, pageable);
    }

    @Override
    public Code save(Code code) {
        if (!StringUtils.hasText(code.getCode())) {
            String maxCode = codeRepository.findMaxCodeByCodeGroupId(code.getCodeGroupId());
            code.setCode(nextCode(maxCode));
        }
        return codeRepository.save(code);
    }

    private String nextCode(String currentMax) {
        if (!StringUtils.hasText(currentMax)) {
            return "001";
        }
        int next = Integer.parseInt(currentMax) + 1;
        return String.format("%03d", next);
    }

    @Override
    public Code update(Code code) {
        return codeRepository.save(code);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) return;
        codeRepository.deleteById(id);
    }

    @Transactional
    public void deleteByCodeGroupId(Long groupId) {
        if (groupId == null) return;
        codeRepository.deleteByCodeGroupId(groupId);
    }
}
