package com.web.doitcommit.service.bookmark;

import com.web.doitcommit.domain.bookmark.Bookmark;
import com.web.doitcommit.dto.board.BoardIdListDto;

import java.util.List;

public interface BookmarkService {

    Bookmark createBookmark(Long principalId, Long boardId);

    void cancelBookmark(Long principalId, Long boardId);

    void cancelMultipleBookmark(Long principalId, List<Long> boardIdList);
}
