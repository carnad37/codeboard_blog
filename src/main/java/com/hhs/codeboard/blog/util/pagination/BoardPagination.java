package com.hhs.codeboard.blog.util.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class BoardPagination {

    private final static int DEFAULT_PER_PAGE = 15;
    private final static int DEFAULT_PER_LIST_PAGE = 10;

    public BoardPagination(Integer perPage, Integer perListPage, Integer pageIndex) {
        this.totalCount = totalCount;
        this.perPage = perPage;
        this.pageIndex = pageIndex;
        this.perListPage = perListPage;
    }

    public BoardPagination(Integer pageIndex) {
        this(DEFAULT_PER_PAGE, DEFAULT_PER_LIST_PAGE, pageIndex);
    }

    public void init(Integer totalCount) {
        totalCount = totalCount == 0 ? 1 : totalCount;
        //검색 범위 구하기
        this.startIndex = this.perPage * (this.pageIndex - 1);
        this.endIndex = this.startIndex + this.perPage;

        //리스트 페이징 구하기
        this.totalPage = (int) Math.ceil((double)totalCount / this.perPage);
        int listPer = (int) Math.floor((double)this.pageIndex / this.perListPage);
        this.startListIndex = listPer * this.perListPage + 1;
        this.endListIndex = this.startListIndex + this.perListPage > this.totalPage ?
                this.totalPage : this.startListIndex + this.perListPage;
        this.isStart = this.pageIndex < 2;
        this.isEnd = this.totalPage - pageIndex == 0;
    }

    private Integer totalCount;

    private Integer perPage;

    private Integer pageIndex;

    private Integer startIndex;

    private Integer endIndex;

    private Integer totalPage;

    private Integer perListPage;

    private Integer startListIndex;

    private Integer endListIndex;

    private Boolean isEnd;

    private Boolean isStart;

    public Pageable getPageable() {
        return PageRequest.of(this.pageIndex - 1, this.perPage);
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(this.pageIndex - 1, this.perPage, sort);
    }

}
