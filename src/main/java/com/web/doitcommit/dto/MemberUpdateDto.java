package com.web.doitcommit.dto;

import com.web.doitcommit.domain.interestTech.InterestTech;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

    private MultipartFile file;

    private String nickname;

    private String email;

    private Set<InterestTech> interestTechSet = new HashSet<InterestTech>();

    private String position;

    private String role;

    private String githubUrl;

    private String url1;

    private String url2;

    private String pictureUrl;

}
