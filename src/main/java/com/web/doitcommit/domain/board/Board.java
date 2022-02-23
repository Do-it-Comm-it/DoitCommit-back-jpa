package com.web.doitcommit.domain.board;

import com.web.doitcommit.domain.BaseEntity;
import com.web.doitcommit.domain.heart.Heart;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @BatchSize(size = 500)
    @Builder.Default
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> heartList = new ArrayList<>();
}
