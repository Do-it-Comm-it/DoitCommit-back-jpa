package com.web.doitcommit.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.boardHashtag.BoardHashtag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "회원별 - 작성한 게시글 dto")
@Getter
public class BoardOfMemberResDto {

    @Schema(description = "게시글 번호")
    private Long boardId;

    @Schema(description = "글제목")
    private String boardTitle;

    @Schema(description = "글내용")
    private String boardContent;

    @Schema(description = "썸네일")
    private String thumbnailUrl;

    @Schema(description = "게시글 해시태그", nullable = true)
    private List<String> boardHashtagNameList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "등록 날짜", nullable = true)
    private LocalDateTime regDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "수정 날짜", nullable = true)
    private LocalDateTime modDate;

    public BoardOfMemberResDto(Board board, String thumbnailUrl){

        this.boardId = board.getBoardId();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.thumbnailUrl = thumbnailUrl;
        this.boardHashtagNameList = new ArrayList<>();
        this.regDate = board.getRegDate();
        this.modDate = board.getModDate();

        //게시글 해시태크 리스트
        if (board.getBoardHashtag() != null && !board.getBoardHashtag().isEmpty()){
            List<BoardHashtag> boardHashtagList = board.getBoardHashtag();
            for (BoardHashtag boardHashtag : boardHashtagList){
                this.boardHashtagNameList.add(boardHashtag.getHashtagCategory().getTagName());
            }
        }
    }

}
