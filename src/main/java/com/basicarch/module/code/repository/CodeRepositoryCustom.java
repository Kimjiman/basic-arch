package com.basicarch.module.code.repository;

import com.basicarch.module.code.entity.Code;
import com.basicarch.module.code.model.CodeSearchParam;

import java.util.List;

public interface CodeRepositoryCustom {
    List<Code> findAllBy(CodeSearchParam param);
    String findMaxCodeByCodeGroupId(Long codeGroupId);
}
