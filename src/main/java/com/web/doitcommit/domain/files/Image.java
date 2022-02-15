package com.web.doitcommit.domain.files;

import lombok.*;

import javax.persistence.Entity;
import com.web.doitcommit.domain.BaseEntity;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Entity
public abstract class Image extends BaseEntity {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private String fileNm;

    @Column(columnDefinition = "TEXT")
    private String filePath;

}
