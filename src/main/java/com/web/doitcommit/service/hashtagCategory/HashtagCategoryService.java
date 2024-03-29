package com.web.doitcommit.service.hashtagCategory;

import com.web.doitcommit.domain.hashtagCategory.HashtagCategory;
import com.web.doitcommit.domain.hashtagCategory.HashtagCategoryRepository;
import com.web.doitcommit.dto.hashtagCategory.HashtagCategoryResDto;
import com.web.doitcommit.dto.hashtagCategory.PopularTagResDto;
import com.web.doitcommit.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class HashtagCategoryService {

    private final HashtagCategoryRepository hashtagCategoryRepository;
    private final RedisService redisService;

    /**
     * 해시태그 목록 조회
     */
    @Transactional(readOnly = true)
    public List<HashtagCategoryResDto> getBoardTagList() {
        List<HashtagCategory> result = hashtagCategoryRepository.findAll();
        return result.stream().map(tag -> new HashtagCategoryResDto(tag)).collect(Collectors.toList());
    }

    /**
     * Redis 인기태그 갱신
     */
    @Scheduled(cron = "00 00 00 * * ?")
    @Transactional
    public void UpdatePopularTag(){
        //금일 인기태그 리스트 추가
        List<Object[]> popularTagList = hashtagCategoryRepository.getLimitPopularTagListForPeriod(7);
        redisService.setList(popularTagList);

        List<Object[]> result = redisService.getValues(LocalDate.now().toString());

        for (Object[] arr : result){
            log.info("tagId: {}, tagName: {}, count: {}", arr[0], arr[1], arr[2]);
        }

        //전날 인기태그 리스트 제거
        redisService.delPopularTag(LocalDate.now().minusDays(1L).toString());
    }

    /**
     * 상단 8개 인기태그 리스트 - redis 조회
     */
    @Transactional(readOnly = true)
    public List<PopularTagResDto> getLimitPopularTagListFor7Days(){

        List<Object[]> result = redisService.getValues(LocalDate.now().toString());

        return result.stream().map(arr -> new PopularTagResDto(
                (Long) arr[0], (String) arr[1], ((Long) arr[2]).intValue())).collect(Collectors.toList());
    }

    /**
     * 상단 8개 인기태그 리스트 조회 - db 조회
     */
    @Transactional(readOnly = true)
    public List<PopularTagResDto> getLimitPopularTagList(){

        List<Object[]> result = hashtagCategoryRepository.getLimitPopularTagList();

        return result.stream().map(arr -> new PopularTagResDto(
                (Long) arr[0], (String) arr[1], ((Long) arr[2]).intValue())).collect(Collectors.toList());
    }

    /**
     * 지정된 기간동안 {tagId, tagName, count} 모든 태그 리스트
     */
    @Transactional(readOnly = true)
    public List<PopularTagResDto> getAllPopularTagListForPeriod(int period){

        List<Object[]> result = hashtagCategoryRepository.getAllPopularTagListForPeriod(period);

        System.out.println(result.size());

        return result.stream().map(arr -> new PopularTagResDto(
                (Long) arr[0], (String) arr[1], ((Long) arr[2]).intValue())).collect(Collectors.toList());
    }
}
