package com.web.doitcommit.dto.member;

import com.web.doitcommit.domain.files.Image;
import com.web.doitcommit.dto.image.ImageResDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagMemberResDto {

    private Long memberId;

    private String nickname;

    private ImageResDto imageResDto;

    public TagMemberResDto(Long memberId, String nickname, Image image){
        this.memberId = memberId;
        this.nickname = nickname;

        if (image != null){
            ImageResDto imageResDto = new ImageResDto(image.getFilePath(), image.getFileNm());
            this.imageResDto = imageResDto;
        }
    }
}
