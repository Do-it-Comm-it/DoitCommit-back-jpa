package com.web.doitcommit.domain.comment;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","board"})
@Entity
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<TagMember> tagMemberSet = new HashSet<>();

    @Builder.Default
    private String content = "";

    @Builder.Default
    private Boolean isExist = true;


    public void changeContent(String content){
        this.content = content;
    }

    public void remove(){
        this.isExist = false;
    }

    //연관관계 메서드
    public void setBoard(Board board){
        this.board = board;
        board.getCommentList().add(this);
    }

    public void addTagMember(TagMember tagMember){
        this.tagMemberSet.add(tagMember);
        tagMember.setComment(this);
    }
}
