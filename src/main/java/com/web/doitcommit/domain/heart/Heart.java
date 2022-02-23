package com.web.doitcommit.domain.heart;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","board"})
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="heart_uk",
                        columnNames={"member_id", "board_id"}
                )
        }
)
public class Heart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long heartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    //연관관계 메서드
    public void setBoard(Board board){
        this.board = board;
        board.getHeartList().add(this);
    }
}
