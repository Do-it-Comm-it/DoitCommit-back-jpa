package com.web.doitcommit.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.hashtag.BoardHashtag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "게시글 목록 조회 dto")
@Getter
public class BoardListResDto {

    @Schema(description = "게시글 번호")
    private Long boardId;

    @Schema(description = "글쓴이")
    private String writer;

    @Schema(description = "카테고리 아이디")
    private Long categoryId;

    @Schema(description = "글제목")
    private String boardTitle;

    @Schema(description = "글내용")
    private String boardContent;

    @Schema(description = "썸네일")
    private String thumbnail;

    @Schema(description = "조회수")
    private int boardCnt;

    @Schema(description = "태그", nullable = true)
    private List<BoardHashtag> boardHashtag;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "등록 날짜", nullable = true)
    private LocalDateTime regDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "수정 날짜", nullable = true)
    private LocalDateTime modDate;

    public BoardListResDto(Board board){
        boardId = board.getBoardId();
        writer = board.getMember().getNickname();
        boardHashtag = board.getBoardHashtag();
        boardTitle = board.getBoardTitle();
        boardContent = board.getBoardContent();
        categoryId = board.getBoardCategory().getCategoryId();
        thumbnail = board.getThumbnail();
        boardCnt = board.getBoardCnt();
        regDate = board.getRegDate();
        modDate = board.getModDate();
    }

}
