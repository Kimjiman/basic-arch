package com.basicarch.base.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * packageName    : com.basicarch.base.model
 * fileName       : SearchParam
 * author         : KIM JIMAN
 * date           : 25. 8. 5. 화요일
 * description    :
 * ===========================================================
 * DATE           AUTHOR          NOTE
 * -----------------------------------------------------------
 * 25. 8. 5.     KIM JIMAN      First Commit
 */
@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSearchParam<ID> extends BaseObject {
    private List<ID> ids;
    private Long createId;
    private String createTimeStart;
    private String createTimeLast;
    private String updateTimeStart;
    private String updateTimeLast;
}


