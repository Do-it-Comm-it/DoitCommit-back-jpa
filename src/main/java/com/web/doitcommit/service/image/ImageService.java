package com.web.doitcommit.service.image;

import com.web.doitcommit.domain.board.Board;
import com.web.doitcommit.domain.files.*;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.dto.board.BoardImageDto;
import com.web.doitcommit.dto.board.ImageRegDto;
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

    /**
     * S3에서 이미지 삭제
     */
    @Transactional
    public void removeImage(ImageRegDto[] allImageArr,
                            ImageRegDto[] imageArr) {
        List<String> allImageList = new ArrayList<String>();
        List<String> imageList = new ArrayList<String>();
        //반환한 전체 url list
        for (int i = 0; i < allImageArr.length; i++) {
            allImageList.add(allImageArr[i].getFilePath() + "/" + allImageArr[i].getFileNm());
        }
        //등록한 url list
        for (int i = 0; i < imageArr.length; i++) {
            imageList.add(imageArr[i].getFilePath() + "/" + imageArr[i].getFileNm());
        }
        //S3에서 삭제해야할 url
        allImageList.removeAll(imageList);
        if (allImageList.size() != 0) {
            for (int i = 0; i < allImageList.size(); i++) {
                imageRemove(allImageList.get(i));
            }
        }
    }

    /**
     * DB에 이미지 등록
     */
    @Transactional
    public void registerImage(Board board, ImageRegDto[] imageArr) {
        if (imageArr.length != 0) {
            imageBoardDbRegister(board, imageArr);
        }
    }

    /**
     * 게시글 이미지 핸들러
     */
    @Transactional
    public void handlerBoardImage(Board board, BoardImageDto boardImageDto) {

        //s3로 반환해준 전체 이미지 배열
        ImageRegDto[] allImageArr = new ImageRegDto[0];
        if (boardImageDto.getAllImageArr() != null) {
            allImageArr = boardImageDto.getAllImageArr();
        }

        //실제 등록하는 이미지 배열
        ImageRegDto[] imageArr = new ImageRegDto[0];
        if (boardImageDto.getImageArr() != null) {
            imageArr = boardImageDto.getImageArr();
        }

        //S3에서 이미지 삭제
        if (allImageArr.length != 0) {
            removeImage(allImageArr, imageArr);
        }
        //DB에 이미지 등록
        if (imageArr.length != 0) {
            registerImage(board, imageArr);
        }

        //수정하면서 지운 이미지 DB, S3에서 삭제
        if (boardImageDto.getDeletedImageArr() != null) {
            for (Long imageId : boardImageDto.getDeletedImageArr()) {
                imageRemove(imageId);
            }
        }
    }
}
