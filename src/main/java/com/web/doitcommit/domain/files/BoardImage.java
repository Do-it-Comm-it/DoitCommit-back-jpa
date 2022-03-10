package com.web.doitcommit.domain.files;

import com.web.doitcommit.domain.board.Board;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"board"})
@DiscriminatorValue("board")
@Entity
public class BoardImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id",  nullable = false)
    private Board board;

    protected BoardImage(){}

    public BoardImage(Board board, String filePath, String fileNm){
        super(board.getBoardId(), filePath, fileNm);
        this.board = board;
        board.setBoardImage(this);
    }
}
