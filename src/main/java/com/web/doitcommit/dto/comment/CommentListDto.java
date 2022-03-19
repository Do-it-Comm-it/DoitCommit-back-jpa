package com.web.doitcommit.dto.comment;

import com.web.doitcommit.dto.memberTag.MemberTagResDto;
import com.web.doitcommit.dto.page.ScrollResultDto;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "댓글 수")
    private long commentCount;

    @Schema(description = "댓글 리스트")
    private ScrollResultDto<CommentResDto, Object[]> commentResDtoList;

    @Schema(description = "회원 태그 리스트")
    @Builder.Default
    private List<MemberTagResDto> memberTagResDtoList = new ArrayList<>();

}
