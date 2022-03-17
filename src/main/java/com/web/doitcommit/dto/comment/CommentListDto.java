package com.web.doitcommit.dto.comment;

import com.web.doitcommit.dto.memberTag.MemberTagResDto;
import com.web.doitcommit.dto.page.ScrollResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CommentListDto {

    private long commentCount;

    private ScrollResultDto<CommentResDto, Object[]> commentResDtoList;

    @Builder.Default
    private List<MemberTagResDto> memberTagResDtoList = new ArrayList<>();

}
