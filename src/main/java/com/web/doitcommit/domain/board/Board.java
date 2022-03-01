package com.web.doitcommit.domain.board;

import com.web.doitcommit.domain.BaseEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BoardCategory boardCategory;

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

    @BatchSize(size = 500)
    @Builder.Default
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> heartList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Bookmark> bookmarkList = new ArrayList<>();
}
