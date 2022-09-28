package com.web.doitcommit.domain.boardHistory;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","board"})
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="board_history_uk",
                        columnNames={"member_id", "board_id"}
                )
        }
)
@Entity
public class BoardHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private LocalDateTime viewDateTime;

    public void changeViewDateTimeToNow(){
        this.viewDateTime = LocalDateTime.now();
    }
}
