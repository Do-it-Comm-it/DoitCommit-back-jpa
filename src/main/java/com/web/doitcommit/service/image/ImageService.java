package com.web.doitcommit.service.image;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.files.*;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.dto.image.ImageForEditorRegDto;
import com.web.doitcommit.dto.image.ImageRegDto;
import com.web.doitcommit.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        MemberImage memberImage = new MemberImage(member, getImage(fileMap.get("filePath"), fileMap.get("fileNm")), false, fileMap.get("filePath"), fileMap.get("fileNm"));
        memberImageRepository.save(memberImage);
        return memberImage.getImageId();
    }

    /**
     * s3 이미지 저장 후 url 반환
     */
    @Transactional
    public Map<String, Object> imageEditorS3Register(MultipartFile imageFile) throws IOException {
        Map<String, String> fileMap = s3Uploader.S3Upload(imageFile);
        String filePath = fileMap.get("filePath");
        String fileNm = fileMap.get("fileNm");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("url", getImage(filePath, fileNm));
        resultMap.put("fileMap", fileMap);
        return resultMap;
    }

    /**
     * 이미지 저장 (DB)
     */
    @Transactional
    public void imageEditorDbRegister(Object board, List<ImageRegDto> imageList) {
        for (int i = 0; i <imageList.size(); i++) {
            BoardImage boardImage = new BoardImage((Board)board, imageList.get(i).getFilePath(), imageList.get(i).getFileNm());
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
    public void imageRemoveFromS3(String storeKey) {
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
     * 글에 등록되지 않은 이미지 S3에서 삭제
     */
    @Transactional
    public void removeUnregisteredImageListFromS3(List<ImageRegDto> allImageList,
                                                  List<ImageRegDto> imageList) {
        List<String> allImageUrlList = new ArrayList<>();
        List<String> imageUrlList = new ArrayList<>();
        //반환한 전체 url list
        for (ImageRegDto image : allImageList) {
            allImageUrlList.add(image.getFilePath() + "/" + image.getFileNm());
        }
        //등록한 url list
        for (ImageRegDto image : imageList) {
            imageUrlList.add(image.getFilePath() + "/" + image.getFileNm());
        }
        //S3에서 삭제해야할 url
        allImageUrlList.removeAll(imageUrlList);
        if (!allImageUrlList.isEmpty()) {
            for (String imageUrl : allImageUrlList) {
                imageRemoveFromS3(imageUrl);
            }
        }
    }

    /**
     * DB에 이미지 등록
     */
    @Transactional
    public void registerImage(Board board, List<ImageRegDto> imageList) {
        if (!imageList.isEmpty()) {
            imageEditorDbRegister(board, imageList);
        }
    }

    /**
     * 게시글 이미지 핸들러
     */
    @Transactional
    public void handleEditorImage(Board board, ImageForEditorRegDto imageForEditorRegDto) {

        //s3로 반환해준 전체 이미지 리스트
        List<ImageRegDto> allImageList = imageForEditorRegDto.getAllImageList();

        //실제 등록하는 이미지 리스트
        List<ImageRegDto> imageList = imageForEditorRegDto.getImageList();

        //수정하면서 지운 이미지 리스트
        List<Long> deletedImageList = imageForEditorRegDto.getDeletedImageList();

        //S3에서 이미지 삭제
        if (!allImageList.isEmpty()) {
            removeUnregisteredImageListFromS3(allImageList, imageList);
        }
        //DB에 이미지 등록
        if (!imageList.isEmpty()) {
            registerImage(board, imageList);
        }

        //수정하면서 지운 이미지 DB, S3에서 삭제
        if (deletedImageList != null && !deletedImageList.isEmpty()) {
            for (Long imageId : imageForEditorRegDto.getDeletedImageList()) {
                imageRemove(imageId);
            }
        }
    }
}
