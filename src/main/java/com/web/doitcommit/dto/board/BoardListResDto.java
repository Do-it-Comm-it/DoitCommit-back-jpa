package com.web.doitcommit.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.files.Image;
import com.web.doitcommit.domain.hashtag.BoardHashtag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private boolean myHeart;

    @Schema(description = "북마크유무")
    private boolean myBookmark;

    //TODO BoardHashtagResDto 필요
    @Schema(description = "게시글 해시태그", nullable = true)
    private List<String> boardHashtagNameList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "등록 날짜", nullable = true)
    private LocalDateTime regDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "수정 날짜", nullable = true)
    private LocalDateTime modDate;


   public BoardListResDto(Board board, String writerImageUrl, String thumbnailUrl, int commentCnt, int heartCnt, Long principalId){
       this.boardId = board.getBoardId();
       this.writerId = board.getMember().getMemberId();
       this.writer = board.getMember().getNickname();
       this.writerImageUrl = writerImageUrl;
       this.boardTitle = board.getBoardTitle();
       this.boardContent = board.getBoardContent();
       this.thumbnailUrl = thumbnailUrl;
       this.commentCnt = commentCnt;
       this.boardCnt = board.getBoardCnt();
       this.heartCnt = heartCnt;
       this.myHeart = board.getHeartList().stream().anyMatch(heart->heart.getMember().getMemberId().equals(principalId) ? true: false);
       this.myBookmark = board.getBookmarkList().stream().anyMatch(bookmark->bookmark.getMember().getMemberId().equals(principalId) ? true: false);
       this.boardHashtagNameList = new ArrayList<>();
       this.regDate = board.getRegDate();
       this.modDate = board.getModDate();


       //게시글 해시태크 리스트
       if (board.getBoardHashtag() != null && !board.getBoardHashtag().isEmpty()){
           List<BoardHashtag> boardHashtagList = board.getBoardHashtag();
           for (BoardHashtag boardHashtag : boardHashtagList){
               this.boardHashtagNameList.add(boardHashtag.getTagCategory().getTagName());
           }
       }
    }

}
