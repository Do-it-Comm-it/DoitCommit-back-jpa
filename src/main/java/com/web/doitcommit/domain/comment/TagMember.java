package com.web.doitcommit.domain.comment;

import com.web.doitcommit.domain.member.Member;
import lombok.*;
import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"comment","member"})
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="tag_member_uk",
                        columnNames={"comment_id", "member_id"}
                )
        }
)
public class TagMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void setComment(Comment comment){
        this.comment = comment;
    }
}
