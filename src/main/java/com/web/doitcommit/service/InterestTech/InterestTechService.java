package com.web.doitcommit.service.InterestTech;

import com.web.doitcommit.domain.interestTech.InterestTech;
import com.web.doitcommit.domain.interestTech.InterestTechRepository;
import com.web.doitcommit.dto.interestTechDto.InterestTechDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class InterestTechService {

    private final InterestTechRepository interestTechRepository;

    /**
     * 관심분야 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<InterestTechDto> getInterestTechList(){
        List<InterestTech> result = interestTechRepository.findAll();
        return result.stream().map(InterestTechDto::new).collect(Collectors.toList());
    }
}
