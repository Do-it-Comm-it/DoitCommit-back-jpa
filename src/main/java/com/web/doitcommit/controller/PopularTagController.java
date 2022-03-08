package com.web.doitcommit.controller;

import com.web.doitcommit.dto.CMRespDto;
import com.web.doitcommit.dto.popularTag.PoplarTagResDto;
import com.web.doitcommit.service.popularTag.PopularTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PopularTagController {

    private final PopularTagService popularTagService;

    /**
     * 7일간의 인기태그 ALL 리스트
     */
    @GetMapping("/popularTags")
    public ResponseEntity<?> getAllPopularTagList() {
        List<PoplarTagResDto> popularTagResDtoList = popularTagService.getAllPopularTagList();

        return new ResponseEntity<>(new CMRespDto<>(
                1, "인기태그 리스트 불러오기 성공", popularTagResDtoList), HttpStatus.OK);
    }
}


