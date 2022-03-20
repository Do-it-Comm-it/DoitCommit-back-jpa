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
    private String id;

    @Schema(description = "회원 닉네임")
    private String display;

    @Schema(description = "회원 프로필")
    private ImageResDto imageResDto;

    public MemberTagResDto(Long memberId, String nickname, Image image){
        this.id = memberId.toString();
        this.display = nickname;

        if (image != null){
            this.imageResDto = new ImageResDto(image.getFilePath(), image.getFileNm());
        }
    }
}
