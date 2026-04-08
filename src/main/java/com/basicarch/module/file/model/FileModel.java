package com.basicarch.module.file.model;

import com.basicarch.base.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * packageName    : com.basicarch.module.file.model
 * fileName       : FileModel
 * author         : KIM JIMAN
 * date           : 26. 2. 11. 수요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 26. 2. 11.     KIM JIMAN      First Commit
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileModel extends BaseModel<Long> {
    private String refPath; // 참조 위치
    private Long refId; // 참조아이디
    private String oriName; // 원래이름
    private String newName; // 새이름
    private String savePath; // 저장경로
    private String ext; // 확장자
    private String type; // mime타입
    private Long size; // 사이즈
}
