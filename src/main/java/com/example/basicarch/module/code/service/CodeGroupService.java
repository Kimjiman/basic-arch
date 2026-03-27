package com.example.basicarch.module.code.service;

import com.example.basicarch.base.service.BaseService;
import com.example.basicarch.base.utils.StringUtils;
import com.example.basicarch.module.code.entity.CodeGroup;
import com.example.basicarch.module.code.model.CodeGroupSearchParam;
import com.example.basicarch.module.code.repository.CodeGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CodeGroupService implements BaseService<CodeGroup, CodeGroupSearchParam, Long> {
    private final CodeGroupRepository codeGroupRepository;

    @Override
    public boolean existsById(Long id) {
        return codeGroupRepository.existsById(id);
    }

    @Override
    public Optional<CodeGroup> findById(Long id) {
        return codeGroupRepository.findById(id);
    }

    public Optional<CodeGroup> findByIdWithCodes(Long id) {
        return codeGroupRepository.findWithCodesById(id);
    }

    @Override
    public List<CodeGroup> findAllBy(CodeGroupSearchParam param) {
        return codeGroupRepository.findAllBy(param);
    }

    @Override
    public CodeGroup save(CodeGroup codeGroup) {
        if (StringUtils.isBlank(codeGroup.getCodeGroup())) {
            String maxCodeGroup = codeGroupRepository.findMaxCodeGroup();
            codeGroup.setCodeGroup(nextCode(maxCodeGroup));
        }
        return codeGroupRepository.save(codeGroup);
    }

    private String nextCode(String currentMax) {
        if (StringUtils.isBlank(currentMax)) {
            return "001";
        }
        int next = Integer.parseInt(currentMax) + 1;
        return String.format("%03d", next);
    }

    @Override
    public CodeGroup update(CodeGroup codeGroup) {
        return codeGroupRepository.save(codeGroup);
    }

    @Override
    public void deleteById(Long id) {
        codeGroupRepository.deleteById(id);
    }
}
