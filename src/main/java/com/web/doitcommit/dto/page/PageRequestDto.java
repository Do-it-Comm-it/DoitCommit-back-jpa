package com.web.doitcommit.dto.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDto {

    @Schema(description = "요청 페이지")
    private int page;
    @Schema(description = "페이지 크기")
    private int size;
    @Schema(description = "검색어")
    private String keyword;
    @Schema(description = "해시태그 고유값")
    private Long tagCategoryId;
    @Schema(description = "게시글 카테고리 고유값")
    private Long boardCategoryId;
    @Schema(description = "정렬 기준")
    private String sortType;


    public PageRequestDto(){
        this.page = 1;
        this.size = 5;
        this.sortType = "DESC";
    }

    public Pageable getPageable(){
        return PageRequest.of(this.page-1, this.size);
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(this.page-1, this.size, sort);
    }


}
