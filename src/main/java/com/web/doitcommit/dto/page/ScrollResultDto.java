package com.web.doitcommit.dto.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class ScrollResultDto<DTO, EN> {

    //DTO리스트
    @Schema(description = "dto 리스트")
    private List<DTO> dtoList;

    //총 페이지 번호
    @Schema(description = "전체 페이지 수")
    private int totalPage;

    //현재 페이지 번호
    @Schema(description = "현재 페이지")
    private int page;

    //목록 사이즈
    @Schema(description = "페이지 크기")
    private int size;

    //마지막
    @Schema(description = "현재 페이지 마지막 여부")
    private boolean last;


    public ScrollResultDto(Page<EN> result, Function<EN,DTO> fn ){

        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        last = result.isLast();
        makePageList(result.getPageable());
    }


    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();
    }


}
