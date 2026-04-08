package com.basicarch.module.code.entity;

import com.basicarch.base.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "code_group")
public class CodeGroup extends BaseEntity<Long> {
    @Column(name = "code_group", nullable = false)
    private String codeGroup;

    @Column(name = "name", nullable = false)
    private String name;

    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "code_group_id", insertable = false, updatable = false)
    private List<Code> codeList;
}
