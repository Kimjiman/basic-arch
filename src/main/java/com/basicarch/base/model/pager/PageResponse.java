package com.basicarch.base.model.pager;

import com.basicarch.base.model.BaseEntity;
import com.basicarch.base.model.BaseModel;
import com.basicarch.base.model.BaseObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> extends BaseObject {
    private PageInfo pageInfo;
    private List<T> list;

    public PageResponse(PageInfo pageInfo, List<T> list) {
        this.pageInfo = pageInfo;
        this.list = list;
        assignRowNumbers();
    }

    private void assignRowNumbers() {
        if (this.list == null || this.list.isEmpty() || this.pageInfo == null) {
            return;
        }

        long startRowNum = this.pageInfo.getTotalRow() - ((this.pageInfo.getPage() - 1) * this.pageInfo.getLimit());
        for (int i = 0; i < this.list.size(); i++) {
            T item = this.list.get(i);
            if (item instanceof BaseModel) {
                ((BaseModel<?>) item).setRowNum(startRowNum - i);
            } else if (item instanceof BaseEntity) {
                ((BaseEntity<?>) item).setRowNum(startRowNum - i);
            }
        }
    }
}
