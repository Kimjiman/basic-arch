package com.basicarch.module.user.repository;

import com.basicarch.module.user.entity.QUser;
import com.basicarch.module.user.entity.User;
import com.basicarch.module.user.model.UserSearchParam;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findAllBy(UserSearchParam param) {
        QUser user = QUser.user;
        BooleanBuilder builder = buildWhere(param);

        return queryFactory.selectFrom(user)
                .where(builder)
                .orderBy(user.id.asc())
                .fetch();
    }

    private BooleanBuilder buildWhere(UserSearchParam param) {
        QUser user = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();

        if (param.getLoginId() != null) {
            builder.and(user.loginId.contains(param.getLoginId()));
        }
        if (param.getName() != null) {
            builder.and(user.name.contains(param.getName()));
        }

        return builder;
    }
}
