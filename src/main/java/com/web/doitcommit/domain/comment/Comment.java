package com.web.doitcommit.domain.comment;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","board","parent"})
@Entity
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Builder.Default
    private String content = "";

    @Builder.Default
    private Boolean isExist = true;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Comment> childList  = new ArrayList<>();

    public void changeContent(String content){
        this.content = content;
    }

    public void removeComment(){
        this.isExist = false;
    }

    //연관관계 메서드
    public void setBoard(Board board){
        this.board = board;
        board.getCommentList().add(this);
    }

    public void setParent(Comment parent){
        this.parent = parent;
        parent.getChildList().add(this);
    }

}
