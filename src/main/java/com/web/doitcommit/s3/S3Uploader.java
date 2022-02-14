package com.web.doitcommit.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.web.doitcommit.domain.files.Files;
import com.web.doitcommit.domain.files.FilesRepository;
import com.web.doitcommit.domain.member.AuthProvider;
import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Component
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final FilesRepository filesRepository;

    /**
     * S3파일 업로드
     */
    public Files S3Upload(MultipartFile multipartFile) throws IOException {

        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        //폴더경로
        String folderPath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        //파일이름
        String fileNm = UUID.randomUUID() + uploadFile.getName();

        //S3에 저장될 위치 + 저장파일명
        String storeKey = folderPath + "/" + fileNm;

        //s3로 업로드
        putS3(uploadFile, storeKey);

        //File 로 전환되면서 로컬에 생성된 파일을 제거
        removeNewFile(uploadFile);

        Files files = Files.builder()
                .filePath(folderPath)
                .fileNm(fileNm)
                .build();

        return files;
    }

    /**
     * S3파일 삭제
     */
    public void delete(Long fileId) {
        try{
            Files fileEntity = filesRepository.findById(fileId).orElseThrow(() ->
                    new IllegalArgumentException("존재하지 않은 파일입니다."));
            String storeKey = fileEntity.getFilePath() + File.separator + fileEntity.getFileNm();
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, storeKey));
        }catch(Exception e) {
            log.error("delete file error"+e.getMessage());
        }
    }

    /**
     * s3 파일 URL
     */
    public String getPictureUrl(String storeKey){
        return amazonS3.getUrl(bucket,storeKey).toString();
    }


    // S3로 업로드
    private PutObjectResult putS3(File uploadFile, String storeKey) {
        return amazonS3.putObject(new PutObjectRequest(bucket, storeKey, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile  multipartFile) throws IOException {

        //파일 이름
        String originalFilename = multipartFile.getOriginalFilename();

        //파일 저장 이름
        String storeFileName = createStoreFileName(originalFilename);

        File convertFile = new File(System.getProperty("user.dir") + "/" + storeFileName);

        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private String createStoreFileName(String originalFileName) {

        String uuid = UUID.randomUUID().toString();

        return uuid + "_" + originalFileName;
    }
}
