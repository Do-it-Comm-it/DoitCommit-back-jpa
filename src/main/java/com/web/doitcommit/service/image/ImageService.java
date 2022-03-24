package com.web.doitcommit.service.image;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.files.*;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.dto.board.ImageRegDto;
import com.web.doitcommit.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class ImageService {

    private final MemberImageRepository memberImageRepository;
    private final BoardImageRepository boardImageRepository;
    private final S3Uploader s3Uploader;
    private final ImageRepository imageRepository;

    /**
     * 회원이미지 저장
     */
    @Transactional
    public Long imageMemberRegister(Member member, MultipartFile imageFile) throws IOException {
        Map<String, String> fileMap = s3Uploader.S3Upload(imageFile);
        MemberImage memberImage = new MemberImage(member, fileMap.get("filePath"), fileMap.get("fileNm"));
        memberImageRepository.save(memberImage);
        return memberImage.getImageId();
    }

    /**
     * 게시글 이미지 저장 (DB)
     */
    @Transactional
    public void imageBoardDbRegister(Board board, ImageRegDto[] imageArr) {
        for (int i = 0; i < imageArr.length; i++) {
            BoardImage boardImage = new BoardImage(board, imageArr[i].getFilePath(), imageArr[i].getFileNm());
            boardImageRepository.save(boardImage);
        }
    }

    /**
     * 이미지 삭제 (DB, S3)
     */
    @Transactional
    public void imageRemove(Long imageId) {
        s3Uploader.delete(imageId);
        imageRepository.deleteById(imageId);
    }

    /**
     * 이미지 삭제 (S3)
     */
    @Transactional
    public void imageRemove(String storeKey) {
        s3Uploader.delete(storeKey);
    }

    /**
     * 이미지 조회
     */
    @Transactional(readOnly = true)
    public String getImage(String filePath, String fileNm) {
        return s3Uploader.getImageUrl(filePath, fileNm);
    }

    /**
     * s3 url 반환
     */
    @Transactional
    public Map<String, Object> imageBoardRegister(MultipartFile imageFile) throws IOException {
        Map<String, String> fileMap = s3Uploader.S3Upload(imageFile);
        String filePath = fileMap.get("filePath");
        String fileNm = fileMap.get("fileNm");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("url", getImage(filePath, fileNm));
        resultMap.put("fileMap", fileMap);
        return resultMap;
    }
}
