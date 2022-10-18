package com.web.doitcommit.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.dto.image.ImageIdResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "게시글 조회 dto")
@Data
public class BoardResDto {

    @Schema(description = "게시글 번호")
    private Long boardId;

    @Schema(description = "글쓴이")
    private String writer;

    @Schema(description = "글쓴이 아이디")
    private Long writerId;

    @Schema(description = "카테고리 아이디")
    private Long categoryId;

    @Schema(description = "글제목")
    private String boardTitle;

    @Schema(description = "글내용")
    private String boardContent;

    @Schema(description = "조회수")
    private int boardCnt;

    @Schema(description = "태그", nullable = true)
    private List boardHashtag;

    @Schema(description = "북마크유무")
    private boolean myBookmark = false;

    @Schema(description = "좋아요유무")
    private boolean myHeart = false;

    @Schema(description = "이미지 배열")
    private List<ImageIdResDto> savedImageIdsAndUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "등록 날짜", nullable = true)
    private LocalDateTime regDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "수정 날짜", nullable = true)
    private LocalDateTime modDate;

    public BoardResDto(Board board){
        boardId = board.getBoardId();
        writer = board.getMember().getNickname();
        writerId = board.getMember().getMemberId();
        boardTitle = board.getBoardTitle();
        boardContent = board.getBoardContent();
        categoryId = board.getBoardCategory().getCategoryId();
        boardCnt = board.getBoardCnt();
        regDate = board.getRegDate();
        modDate = board.getModDate();
    }

    public void changeMyBookmark(Boolean myBookmark){
        this.myBookmark = myBookmark;
    }

    public void changeMyHeart(Boolean myHeart){
        this.myHeart = myHeart;
    }

    public void changeSavedImageIdsAndUrl(List<ImageIdResDto> savedImageIdsAndUrl){
        this.savedImageIdsAndUrl = savedImageIdsAndUrl;
    }
}
