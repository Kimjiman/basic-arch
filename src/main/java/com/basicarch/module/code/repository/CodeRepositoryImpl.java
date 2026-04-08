package com.basicarch.module.code.repository;

import com.basicarch.module.code.entity.Code;
import com.basicarch.module.code.entity.QCode;
import com.basicarch.module.code.entity.QCodeGroup;
import com.basicarch.module.code.model.CodeSearchParam;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CodeRepositoryImpl implements CodeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Code> findAllBy(CodeSearchParam param) {
        return createBaseQuery(param)
                .orderBy(QCode.code1.codeGroupId.asc())
                .fetch();
    }

    @Override
    public String findMaxCodeByCodeGroupId(Long codeGroupId) {
        QCode code = QCode.code1;
        return queryFactory.select(code.code.max())
                .from(code)
                .where(code.codeGroupId.eq(codeGroupId))
                .fetchOne();
    }

    private JPAQuery<Code> createBaseQuery(CodeSearchParam param) {
        QCode code = QCode.code1;
        QCodeGroup codeGroup = QCodeGroup.codeGroup1;
        BooleanBuilder builder = buildWhere(param);

        return queryFactory.select(Projections.fields(Code.class,
                        code.id,
                        code.codeGroupId,
                        code.code,
                        code.name,
                        code.info,
                        code.createTime,
                        code.createId,
                        code.updateTime,
                        code.updateId,
                        codeGroup.codeGroup.as("codeGroup"),
                        codeGroup.name.as("codeGroupName")
                ))
                .from(code)
                .leftJoin(codeGroup).on(code.codeGroupId.eq(codeGroup.id))
                .where(builder);
    }

    private BooleanBuilder buildWhere(CodeSearchParam param) {
        QCode code = QCode.code1;
        BooleanBuilder builder = new BooleanBuilder();

        if (param.getCodeGroupId() != null) {
            builder.and(code.codeGroupId.eq(param.getCodeGroupId()));
        }
        if (param.getName() != null) {
            builder.and(code.name.contains(param.getName()));
        }

        return builder;
    }
}
