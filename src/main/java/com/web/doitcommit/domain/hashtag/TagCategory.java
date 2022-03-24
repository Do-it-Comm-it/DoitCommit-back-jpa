package com.web.doitcommit.domain.hashtag;

import lombok.*;

import javax.persistence.*;

/**
 * 태그 종류 카테고리입니다
 * ex) 직장인, 공대생, 취준생 등
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class TagCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(unique = true, nullable = false)
    private String tagName;

    public TagCategory(Long tagId) {
        this.tagId = tagId;
    }
}