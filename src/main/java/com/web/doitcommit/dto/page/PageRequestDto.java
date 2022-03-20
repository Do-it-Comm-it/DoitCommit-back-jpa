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

@Builder
@AllArgsConstructor
@Data
public class PageRequestDto {

    @Schema(description = "요청 페이지")
    private int page;
    @Schema(description = "페이지 크기")
    private int size;


    public PageRequestDto(){
        this.page = 1;
        this.size = 5;
    }

    public Pageable getPageable(){
        return PageRequest.of(this.page-1, this.size);
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(this.page-1, this.size, sort);
    }


}
