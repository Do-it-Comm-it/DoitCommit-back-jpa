package com.web.doitcommit.domain.board;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @Column(nullable = false)
    private String categoryId;

    @Column(nullable = false)
    private String boardTitle;

    @Column(columnDefinition = "TEXT")
    private String boardContent;

    @Builder.Default
    @Column(nullable = false)
    private int boardCnt = 0;
}
