package com.web.doitcommit.service.files;

import com.web.doitcommit.domain.files.FilesRepository;
import com.web.doitcommit.domain.files.Files;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FilesService {

    private final FilesRepository filesRepository;

    /**
     * 파일 저장
    */
    public Long fileRegister(Files files) {
        Files file = filesRepository.save(files);
        return file.getId();
    }

    /**
     * 파일 삭제
     */
    public void fileRemove(Long fileNo) {
        filesRepository.deleteById(fileNo);
    }
}
