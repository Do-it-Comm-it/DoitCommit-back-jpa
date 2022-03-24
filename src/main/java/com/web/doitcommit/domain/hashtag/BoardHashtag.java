package com.web.doitcommit.domain.hashtag;

import com.web.doitcommit.domain.board.Board;
import lombok.*;

import javax.persistence.*;

@ToString(exclude = {"board"})
@Getter
@Entity
public class BoardHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private TagCategory tagCategory;

    protected BoardHashtag(){}

    public BoardHashtag(Board board, TagCategory tagCategory) {
        this.board = board;
        this.tagCategory = tagCategory;
        board.setBoardHashtag(this);
    }
}
