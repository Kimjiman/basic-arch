package com.basicarch.module.user.entity;

import com.basicarch.base.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * 사용자-역할 매핑 테이블 (N:M 관계를 풀어낸 중간 테이블).
 * - userId: user 테이블의 PK. 한 사용자에 여러 역할을 부여할 수 있음.
 * - roleId: role 테이블의 PK.
 *
 * 로그인 시 AuthUserDetailsService에서 userId로 조회하여 해당 사용자의 역할 목록을 가져온다.
 * UserRole이 없는 사용자는 기본 역할 "USR"이 부여된다.
 */
@Entity
@Table(name = "user_role")
public class UserRole extends BaseEntity<Long> {
    /** user 테이블의 PK (FK) */
    @Column(name = "user_id")
    private Long userId;

    /** role 테이블의 PK (FK) */
    @Column(name = "role_id")
    private Long roleId;
}
