package com.web.doitcommit.dto.board;

import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.dto.image.ImageResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(description = "회원별 - 작성한 게시글 목록 조회 dto")
@Getter
public class BoardListOfMemberResDto {

    @Schema(description = "회원 고유값(작성자)")
    private Long memberId;

    @Schema(description = "회원 닉네임")
    private String nickname;

    @Schema(description = "포지션")
    private String position;

    @Schema(description = "작성자 프로필 이미지 URL")
    private String memberImageUrl;

    @Schema(description = "회원이 작성한 게시글 총 개수")
    private long totalBoardCnt;

    @Schema(description = "회원이 작성한 게시글 리스트")
    private List<BoardOfMemberResDto> boardOfMemberResDtoList;

    public BoardListOfMemberResDto(Member member, String memberImageUrl, long totalBoardCnt,
                                   List<BoardOfMemberResDto> boardOfMemberResDtoList){

        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.position = member.getPosition();
        this.memberImageUrl = memberImageUrl;
        this.totalBoardCnt = totalBoardCnt;
        this.boardOfMemberResDtoList = boardOfMemberResDtoList;
    }
}
