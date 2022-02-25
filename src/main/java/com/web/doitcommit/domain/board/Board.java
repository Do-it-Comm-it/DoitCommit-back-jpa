package com.web.doitcommit.domain.board;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @Column(nullable = false)
    private String categoryId;

    @Column(nullable = false)
    private String boardTitle;

    @Column
    private String thumbnail;

    @Column(columnDefinition = "TEXT")
    private String boardContent;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "board_id"))
    @Builder.Default
    private Set<String> tag = new HashSet<>();

    @Builder.Default
    @Column(nullable = false)
    private int boardCnt = 0;
}
