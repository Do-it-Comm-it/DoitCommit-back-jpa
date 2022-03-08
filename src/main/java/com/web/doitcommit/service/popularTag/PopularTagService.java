package com.web.doitcommit.service.popularTag;

import com.web.doitcommit.domain.board.BoardRepository;
import com.web.doitcommit.dto.popularTag.PoplarTagResDto;
import com.web.doitcommit.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PopularTagService {

    private final BoardRepository boardRepository;
    private final RedisService redisService;

    /**
     * Redis 인기태그 갱신
     */
    @Scheduled(cron = "00 00 00 * * ?")
    @Transactional
    public void UpdatePopularTag(){
        //금일 인기태그 리스트 추가
        List<Object[]> popularTagList = boardRepository.getLimitPopularTag();
        redisService.setList(popularTagList);

        List<Object[]> result = redisService.getValues(LocalDate.now().toString());

        for (Object[] arr : result){
            log.info("tag: {}, count: {}", arr[0], arr[1]);
        }

        //전날 인기태그 리스트 제거
        redisService.delPopularTag(LocalDate.now().minusDays(1L).toString());
    }


    /**
     * 7일간의 인기태그 ALL 리스트
     */
    @Transactional(readOnly = true)
    public List<PoplarTagResDto> getAllPopularTagList(){

        List<Object[]> result = boardRepository.getAllPopularTag();

        return result.stream().map(arr -> new PoplarTagResDto(
                (String) arr[0], ((BigInteger)arr[1]).intValue())).collect(Collectors.toList());
    }
}
