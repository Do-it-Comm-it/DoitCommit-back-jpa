package com.web.doitcommit.domain.hashtagCategory;

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
public class HashtagCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashtagId;

    @Column(unique = true, nullable = false)
    private String tagName;

    public HashtagCategory(Long hashtagId) {
        this.hashtagId = hashtagId;
    }
}