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

    @Schema(description = "댓글 고유값")
    private Long commentId;

    @Schema(description = "회원 고유값(작성자)")
    private String writerId;

    @Schema(description = "작성자 닉네임")
    private String nickname;

    @Schema(description = "이미지 URL")
    private String imageUrl;

    @Schema(description = "본문")
    private String content;

    @Schema(description = "댓글 존재 유무")
    private Boolean isExist;

    @Schema(description = "댓글 등록 날짜('yyyy-MM-ddTHH:mm')")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @Schema(description = "회원 태그의 회원 고유값 set")
    private Set<String> memberIdSet;

    public CommentResDto(Comment comment, String imageUrl){

        commentId = comment.getCommentId();
        writerId = comment.getMember().getMemberId().toString();
        nickname = comment.getMember().getNickname();
        content = comment.getContent();
        this.imageUrl = imageUrl;
        isExist = comment.getIsExist();
        regDateTime = comment.getModDate(); //수정 날짜
        memberIdSet = comment.getMemberTagSet().stream().map(memberTag ->
                memberTag.getMember().getMemberId().toString()).collect(Collectors.toSet());

    }
}
