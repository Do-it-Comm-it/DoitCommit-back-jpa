package com.web.doitcommit.domain.board;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.boardHashtag.BoardHashtag;
import com.web.doitcommit.domain.boardHistory.BoardHistory;
import com.web.doitcommit.domain.files.BoardImage;
import com.web.doitcommit.domain.heart.Heart;
import com.web.doitcommit.domain.boardCategory.BoardCategory;
import com.web.doitcommit.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import com.web.doitcommit.domain.comment.Comment;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.web.doitcommit.domain.bookmark.Bookmark;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BoardCategory boardCategory;

    @Column(nullable = false)
    private String boardTitle;

    @Column
    private String thumbnail;

    @Column(columnDefinition = "TEXT")
    private String boardContent;

    @Builder.Default
    @Column(nullable = false)
    private int boardCnt = 0;

    @BatchSize(size = 500)
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<BoardImage> boardImageList = new ArrayList<>();

    @BatchSize(size = 500)
    @Builder.Default
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> heartList = new ArrayList<>();

    @BatchSize(size = 500)
    @Builder.Default
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @BatchSize(size = 500)
    @Builder.Default
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @BatchSize(size = 500)
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<BoardHashtag> boardHashtag = new ArrayList<>();

    @BatchSize(size = 500)
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<BoardHistory> boardHistoryList = new ArrayList<>();

    //연관관계 메서드
    public void setBoardImage(BoardImage boardImage){
        this.getBoardImageList().add(boardImage);
    }

    public void setBoardHashtag(BoardHashtag boardHashtag){
        this.boardHashtag.add(boardHashtag);
    }

    public void changeBoardCnt(){
        this.boardCnt += 1;
    }

    //카테고리변경
    public void changeCategoryId(Long categoryId) {
        this.boardCategory = BoardCategory.builder().categoryId(categoryId).build();
    }

    //제목 변경
    public void changeTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    //내용 변경
    public void changeContent(String boardContent) {
        this.boardContent = boardContent;
    }


}
