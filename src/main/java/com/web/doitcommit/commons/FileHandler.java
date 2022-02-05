package com.web.doitcommit.commons;

import com.web.doitcommit.domain.member.Member;
import com.web.doitcommit.dto.member.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class FileHandler {

    public String fileUpload(MultipartFile file) {
            String path = System.getProperty("user.dir") + File.separator;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String str = sdf.format(date); //오늘날짜를 포맷함
            String datePath = str.replace("-", File.separator);
            //폴더생성
            File uploadPath = new File(path, datePath);
            if (!uploadPath.exists()) {
                try {
                    uploadPath.mkdirs();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            //파일저장
            UUID uuid = UUID.randomUUID();
            String imageFileName = path + datePath + File.separator + uuid + "_" + file.getOriginalFilename();
            Path imageFilePath = Paths.get(imageFileName);
            try {
                Files.write(imageFilePath, file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return imageFileName;
    }
}
