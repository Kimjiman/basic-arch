package com.basicarch.module.user.entity;

import com.basicarch.base.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
 * 역할(권한) 마스터 테이블.
 * - roleName: 역할 식별자 (예: "ADM", "USR", "MGR"). Menu.roles 컬럼에 저장되는 값과 동일해야 함.
 * - description: 역할에 대한 설명 (예: "관리자", "일반 사용자")
 * - useYn: 사용 여부. "N"이면 해당 역할은 인증 시 부여되지 않음.
 *
 * AuthUserDetailsService에서 로그인 시 UserRole → Role 조회를 통해 사용자에게 역할을 부여한다.
 */
@Entity
@Table(name = "role")
public class Role extends BaseEntity<Long> {
    /** 역할 식별자 (예: "ADM", "USR"). ROLE_ 접두어 없이 저장. */
    @Column(name = "role_name")
    private String roleName;

    /** 역할 설명 (예: "관리자", "일반 사용자") */
    @Column(name = "description")
    private String description;

    /** 사용 여부 ("Y"/"N"). "N"이면 로그인 시 해당 역할이 부여되지 않음. */
    @Column(name = "use_yn")
    @Builder.Default
    private String useYn = "Y";
}
