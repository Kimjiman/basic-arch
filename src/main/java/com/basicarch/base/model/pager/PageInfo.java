package com.basicarch.base.model.pager;

import com.basicarch.base.model.BaseObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageInfo extends BaseObject {
    private static final int INIT_LIMIT = 10;
    private static final int INIT_PAGE_SIZE = 5;

    private long page;                           // 현재 페이지
    private long limit;                          // 보여줄 글의 개수
    private long offset;                         // 시작페이지 (DB)
    private long totalRow;                       // 총 글의 개수
    private long totalPage;                      // 총 페이지의 개수

    private long startPage;                      // 시작페이지
    private long lastPage;                       // 마지막페이지
    private long prevPage;                       // 이전페이지
    private long nextPage;                       // 다음페이지

    private boolean hasStartPage;                // 시작페이지 체크
    private boolean hasLastPage;                 // 마지막페이지 체크
    private boolean hasPrevPage;                 // 이전페이지 체크
    private boolean hasNextPage;                 // 다음페이지 체크

    private PageInfo(long page, long totalRow, int limit, int pageSize) {
        this.limit = limit <= 0 ? INIT_LIMIT : limit;
        this.totalRow = totalRow < 0 ? 0 : totalRow;
        this.page = page < 1 ? 1 : page;

        this.totalPage = (long) Math.ceil((double) this.totalRow / this.limit);
        this.page = Math.min(this.page, this.totalPage);
        this.offset = (this.page - 1) * this.limit;

        this.startPage = ((this.page - 1) / pageSize) * pageSize + 1;
        this.lastPage = Math.min(this.startPage + pageSize - 1, this.totalPage);

        this.prevPage = this.startPage > 1 ? this.startPage - 1 : 1;
        this.nextPage = this.lastPage < this.totalPage ? this.lastPage + 1 : this.totalPage;

        this.hasStartPage = (this.page > 1);
        this.hasLastPage = (this.page < this.totalPage);
        this.hasPrevPage = (this.startPage > 1);
        this.hasNextPage = (this.lastPage < this.totalPage);
    }

    public static PageInfo of(long page, long totalRow) {
        return new PageInfo(page, totalRow, INIT_LIMIT, INIT_PAGE_SIZE);
    }

    public static PageInfo of(long page, long totalRow, int limit, int pageSize) {
        return new PageInfo(page, totalRow, limit, pageSize);
    }

    public static PageInfo from(Page<?> page) {
        return new PageInfo(
                page.getNumber() + 1,
                page.getTotalElements(),
                page.getSize(),
                INIT_PAGE_SIZE
        );
    }
}
