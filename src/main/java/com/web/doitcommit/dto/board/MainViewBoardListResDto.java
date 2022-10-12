package com.web.doitcommit.dto.board;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.hashtag.BoardHashtag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "메인화면 게시글 목록 조회 dto")
@Getter
public class MainViewBoardListResDto {
    @Schema(description = "게시글 번호")
    private Long boardId;

    @Schema(description = "작성자 프로필 이미지 URL")
    private String writerImageUrl;

    @Schema(description = "글제목")
    private String boardTitle;

    @Schema(description = "글내용")
    private String boardContent;

    @Schema(description = "댓글수")
    private int commentCnt;

    @Schema(description = "북마크유무")
    private boolean myBookmark;

    @Schema(description = "게시글 해시태그", nullable = true)
    private List<String> boardHashtagNameList;

    @Schema(description = "좋아요 수")
    private int heartCnt;

    @Schema(description = "좋아요유무")
    private boolean myHeart;

    @Schema(description = "썸네일")
    private String thumbnailUrl;

    public MainViewBoardListResDto(Board board, String writerImageUrl, String thumbnailUrl, int commentCnt, Long principalId){
        this.boardId = board.getBoardId();
        this.writerImageUrl = writerImageUrl;
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.commentCnt = commentCnt;
        this.myBookmark = board.getBookmarkList().stream().anyMatch(bookmark->bookmark.getMember().getMemberId().equals(principalId) ? true: false);
        this.boardHashtagNameList = new ArrayList<>();
        this.heartCnt = board.getHeartList().size();
        this.myHeart = board.getHeartList().stream().anyMatch(heart->heart.getMember().getMemberId().equals(principalId) ? true: false);
        this.thumbnailUrl = thumbnailUrl;

        //게시글 해시태크 리스트
        if (board.getBoardHashtag() != null && !board.getBoardHashtag().isEmpty()){
            List<BoardHashtag> boardHashtagList = board.getBoardHashtag();
            for (BoardHashtag boardHashtag : boardHashtagList){
                this.boardHashtagNameList.add(boardHashtag.getHashtagCategory().getTagName());
            }
        }
    }

}
