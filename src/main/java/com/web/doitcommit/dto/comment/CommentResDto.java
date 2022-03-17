package com.web.doitcommit.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.domain.files.Image;
import com.web.doitcommit.domain.files.MemberImage;
import com.web.doitcommit.dto.image.ImageResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CommentResDto {

    private Long commentId;

    private String writerId;

    private String nickname;

    private ImageResDto imageResDto;

    private String content;

    private Boolean isExist;

    @Schema(description = "투두 날짜('yyyy-MM-ddTHH:mm')")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    private Set<String> memberIdSet;

    public CommentResDto(Comment comment, Image image){

        commentId = comment.getCommentId();
        writerId = comment.getMember().getMemberId().toString();
        nickname = comment.getMember().getNickname();
        content = comment.getContent();
        isExist = comment.getIsExist();
        regDateTime = comment.getModDate(); //수정 날짜
        memberIdSet = comment.getMemberTagSet().stream().map(memberTag ->
                memberTag.getMember().getMemberId().toString()).collect(Collectors.toSet());

        if (image != null){
            this.imageResDto = new ImageResDto(image.getFilePath(), image.getFileNm());
        }
    }
}
