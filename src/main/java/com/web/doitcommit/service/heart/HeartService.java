package com.web.doitcommit.service.heart;

import com.web.doitcommit.domain.heart.Heart;

public interface HeartService {

    Heart heart(Long principalId, Long boardId);

    void cancelHeart(Long principalId, Long boardId);
}
