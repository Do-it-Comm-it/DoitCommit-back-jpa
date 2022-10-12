package com.web.doitcommit.domain.boardHashtag;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.hashtagCategory.HashtagCategory;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@ToString(exclude = {"board"})
@Getter
@Entity
public class BoardHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardHashtagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hashtag_id")
    private HashtagCategory hashtagCategory;

    protected BoardHashtag(){}

    public BoardHashtag(Board board, HashtagCategory hashtagCategory) {
        this.board = board;
        this.hashtagCategory = hashtagCategory;
        board.setBoardHashtag(this);
    }
}
