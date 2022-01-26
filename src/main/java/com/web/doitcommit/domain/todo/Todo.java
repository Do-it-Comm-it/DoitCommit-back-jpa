package com.web.doitcommit.domain.todo;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.member.Member;
import lombok.*;

import javax.persistence.*;

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
    @JoinColumn(name = "memer_id", nullable = false)
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


}
