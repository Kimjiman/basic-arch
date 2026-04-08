package com.basicarch.module.file.entity;

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
@Entity
@Table(name = "file")
public class FileInfo extends BaseEntity<Long> {
    @Column(name = "ref_path")
    private String refPath;

    @Column(name = "ref_id")
    private Long refId;

    @Column(name = "ori_name")
    private String oriName;

    @Column(name = "new_name")
    private String newName;

    @Column(name = "save_path")
    private String savePath;

    @Column(name = "ext")
    private String ext;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private Long size;
}
