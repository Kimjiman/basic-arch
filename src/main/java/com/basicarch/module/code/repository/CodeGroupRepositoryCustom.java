package com.basicarch.module.code.repository;

import com.basicarch.module.code.entity.CodeGroup;
import com.basicarch.module.code.model.CodeGroupSearchParam;

import java.util.List;

public interface CodeGroupRepositoryCustom {
    List<CodeGroup> findAllBy(CodeGroupSearchParam param);
    String findMaxCodeGroup();
}
