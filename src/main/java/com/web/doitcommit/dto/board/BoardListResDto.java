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

    @Schema(description = "회원 고유값(작성자)")
    private Long writerId;

    @Schema(description = "작성자 닉네임")
    private String writer;

    @Schema(description = "작성자 프로필 이미지 URL")
    private String writerImageUrl;

    @Schema(description = "글제목")
    private String boardTitle;

    @Schema(description = "글내용")
    private String boardContent;

    @Schema(description = "썸네일")
    private String thumbnailUrl;

    @Schema(description = "댓글수")
    private int commentCnt;

    @Schema(description = "조회수")
    private int boardCnt;

    @Schema(description = "좋아요 수")
    private int heartCnt;

    @Schema(description = "좋아요유무")
    private boolean myHeart = false;

    @Schema(description = "북마크유무")
    private boolean myBookmark = false;

    @Schema(description = "태그", nullable = true)
    private List<BoardHashtag> boardHashtagList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "등록 날짜", nullable = true)
    private LocalDateTime regDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "수정 날짜", nullable = true)
    private LocalDateTime modDate;

    //TODO boardCategory 관련데이터 필요 여부

   public BoardListResDto(Board board, Long principalId){
        boardId = board.getBoardId();
        writer = board.getMember().getNickname();
//        boardHashtag = board.getBoardHashtag();
        boardTitle = board.getBoardTitle();
        boardContent = board.getBoardContent();
        categoryId = board.getBoardCategory().getCategoryId();
        thumbnailUrl = board.getThumbnail();
        boardCnt = board.getBoardCnt();
        regDate = board.getRegDate();
        modDate = board.getModDate();
        heartCnt = board.getHeartList().size();
        myHeart = board.getHeartList().stream().anyMatch(t->t.getMember().getMemberId().equals(principalId) ? true: false);
        myBookmark = board.getBookmarkList().stream().anyMatch(t->t.getMember().getMemberId().equals(principalId) ? true: false);
    }

}
