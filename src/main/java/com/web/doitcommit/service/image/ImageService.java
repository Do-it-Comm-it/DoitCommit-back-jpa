package com.web.doitcommit.service.image;

import com.web.doitcommit.domain.files.Image;
import com.web.doitcommit.domain.files.ImageRepository;
import com.web.doitcommit.domain.files.MemberImage;
import com.web.doitcommit.domain.files.MemberImageRepository;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class ImageService {

    private final MemberImageRepository memberImageRepository;
    private final S3Uploader s3Uploader;
    private final ImageRepository imageRepository;

    /**
     * 회원이미지 저장
    */
    @Transactional
    public Long imageMemberRegister(Member member, MultipartFile imageFile) throws IOException {

        Map<String, String> fileMap = s3Uploader.S3Upload(imageFile);
        System.out.println("-----------------");
        System.out.println(fileMap.get("filePath"));
        System.out.println(fileMap.get("fileNm"));

        MemberImage memberImage = new MemberImage(member, fileMap.get("filePath"), fileMap.get("fileNm"));

        memberImageRepository.save(memberImage);

        return memberImage.getImageId();
    }


    /**
     * 이미지 삭제
     */
    @Transactional
    public void imageRemove(Long imageId) {
        s3Uploader.delete(imageId);
        imageRepository.deleteById(imageId);
    }

    /**
     * 이미지 조회
     */
    public String getImage(String filePath, String fileNm) {
        return s3Uploader.getImageUrl(filePath, fileNm);
    }
}
