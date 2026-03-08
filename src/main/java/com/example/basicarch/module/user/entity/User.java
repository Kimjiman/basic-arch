package com.example.basicarch.module.user.entity;

import com.example.basicarch.base.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity<Long> {
    @Column(name = "login_id", unique = true, nullable = false)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "use_yn", nullable = false)
    @Builder.Default
    private String useYn = "Y";

    @Transient
    private List<String> roleList;
}
