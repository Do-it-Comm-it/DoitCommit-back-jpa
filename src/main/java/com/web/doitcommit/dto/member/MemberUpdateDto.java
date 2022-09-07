package com.web.doitcommit.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "멤버 업데이트 dto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

    @NotNull
    @Schema(description = "멤버 아이디", nullable = true)
    private Long memberId;

    @Schema(description = "파일정보", nullable = true)
    private MultipartFile imageFile;

    @Schema(description = "닉네임", nullable = true)
    private String nickname;

    @Schema(description = "이메일", nullable = true)
    private String email;

    @Schema(description = "관심기술정보", nullable = true)
    private List<Long> interestTechSet;

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

}
