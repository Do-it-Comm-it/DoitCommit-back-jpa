package com.web.doitcommit.domain.interestTech;

import lombok.*;
import javax.persistence.*;

/**
 * 관심분야 테이블입니다
 * ex) 기획, 디자인, 개발
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class InterestTech {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestTechId;

    @Column(unique = true, nullable = false)
    private String interestTechNm;
    
}
