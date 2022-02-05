package com.web.doitcommit.domain.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member"})
@Entity
public class Todo extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Builder.Default
    @Column(nullable = false)
    private String content = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Importance importance;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isFixed = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isFinished = false;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime todoDateTime = LocalDateTime.now();

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void changeType(TodoType type){
        this.type = type;
    }

    public void changeImportance(Importance importance){
        this.importance = importance;
    }

    public void changeIsFixed(Boolean isFixed){
        this.isFixed = isFixed;
    }

    public void changeIsFinished(Boolean isFinished){
        this.isFinished = isFinished;
    }

    public void changeTodoDateTime(LocalDateTime todoDateTime){
        this.todoDateTime = todoDateTime;
    }

}
