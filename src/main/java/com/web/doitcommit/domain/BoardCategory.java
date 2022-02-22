package com.web.doitcommit.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"parent"})
@Entity
public class BoardCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private BoardCategory parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<BoardCategory> childList = new ArrayList<>();

    //연관관계 메서드
    public void setParent(BoardCategory parent){
        this.parent = parent;
        parent.getChildList().add(this);
    }


}
