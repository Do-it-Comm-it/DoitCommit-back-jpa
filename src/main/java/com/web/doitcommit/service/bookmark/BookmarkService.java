package com.web.doitcommit.service.bookmark;

import com.web.doitcommit.domain.bookmark.Bookmark;

public interface BookmarkService {

    Bookmark createBookmark(Long principalId, Long boardId);

    void cancelBookmark(Long principalId, Long boardId);
}
