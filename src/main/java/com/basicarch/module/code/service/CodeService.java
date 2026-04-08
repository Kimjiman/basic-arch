package com.basicarch.module.code.service;

import com.basicarch.base.exception.CustomException;
import com.basicarch.base.exception.SystemErrorCode;
import com.basicarch.base.service.BaseService;
import com.basicarch.base.utils.StringUtils;
import com.basicarch.module.code.entity.Code;
import com.basicarch.module.code.model.CodeSearchParam;
import com.basicarch.module.code.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Code save(Code code) {
        if (StringUtils.isBlank(code.getCode())) {
            String maxCode = codeRepository.findMaxCodeByCodeGroupId(code.getCodeGroupId());
            code.setCode(nextCode(maxCode));
        }
        return codeRepository.save(code);
    }

    private String nextCode(String currentMax) {
        if (StringUtils.isBlank(currentMax)) {
            return "001";
        }
        int next = Integer.parseInt(currentMax) + 1;
        return String.format("%03d", next);
    }

    @Override
    public Code update(Code code) {
        Code originCode = this.findById(code.getId()).orElseThrow(() -> new CustomException(SystemErrorCode.NOT_FOUND));
        if(StringUtils.isBlank(code.getInfo())) {
            code.setInfo(originCode.getInfo());
        }

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
