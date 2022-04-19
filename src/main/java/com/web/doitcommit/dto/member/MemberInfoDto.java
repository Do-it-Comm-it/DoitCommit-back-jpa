package com.web.doitcommit.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.doitcommit.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Schema(description = "멤버 정보 dto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDto {

    @Schema(description = "닉네임", nullable = true)
    private String nickname;

    @Schema(description = "이메일", nullable = true)
    private String email;

    @Schema(description = "관심기술정보", nullable = true)
    private Set<String> interestTechSet = new HashSet<>();

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "등록 날짜", nullable = true)
    private LocalDateTime regDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "수정 날짜", nullable = true)
    private LocalDateTime modDate;

    public MemberInfoDto(Member member) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.interestTechSet = member.getInterestTechSet();
        this.position = member.getPosition();
        this.githubUrl = member.getGithubUrl();
        this.url1 = member.getUrl1();
        this.url2 = member.getUrl2();
        this.regDate = member.getRegDate();
        this.modDate = member.getModDate();
    }
}
