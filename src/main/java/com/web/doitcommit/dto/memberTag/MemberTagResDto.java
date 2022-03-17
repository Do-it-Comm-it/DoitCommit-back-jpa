package com.web.doitcommit.dto.memberTag;

import com.web.doitcommit.domain.files.Image;
import com.web.doitcommit.dto.image.ImageResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberTagResDto {

    @Schema(description = "회원 고유값 - String 타입")
    private String memberId;

    @Schema(description = "회원 닉네임")
    private String nickname;

    @Schema(description = "회원 프로필")
    private ImageResDto imageResDto;

    public MemberTagResDto(Long memberId, String nickname, Image image){
        this.memberId = memberId.toString();
        this.nickname = nickname;

        if (image != null){
            ImageResDto imageResDto = new ImageResDto(image.getFilePath(), image.getFileNm());
            this.imageResDto = imageResDto;
        }
    }
}
