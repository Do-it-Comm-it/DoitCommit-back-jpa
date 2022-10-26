package com.web.doitcommit.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "멤버 정보 dto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDto {

    @Schema(description = "멤버아이디", nullable = true)
    private Long memberId;

    @Schema(description = "닉네임", nullable = true)
    private String nickname;

    @Schema(description = "이메일", nullable = true)
    private String email;

    @Schema(description = "자기소개", nullable = true)
    private String selfIntro;

    @Schema(description = "관심기술정보", nullable = true)
    private List interestTechSet;

    @Schema(description = "포지션", nullable = true)
    private String position;

    @Schema(description = "깃허브 URL", nullable = true)
    private String githubUrl;

    @Schema(description = "URL1", nullable = true)
    private String url1;

    @Schema(description = "URL2", nullable = true)
    private String url2;

    @Schema(description = "프로필 사진 URL", nullable = true)
    private String pictureUrl;

    @Schema(description = "권한", nullable = true)
    private String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "등록 날짜", nullable = true)
    private LocalDateTime regDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "수정 날짜", nullable = true)
    private LocalDateTime modDate;

    public MemberInfoDto(Member member) {
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.selfIntro = member.getSelfIntro();
        this.position = member.getPosition();
        this.githubUrl = member.getGithubUrl();
        this.url1 = member.getUrl1();
        this.url2 = member.getUrl2();
        this.role = member.getRole();
        this.regDate = member.getRegDate();
        this.modDate = member.getModDate();
    }
}
