package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.dto.comment.CommentUpdateDto;

public interface CommentService {

    Comment register(CommentRegDto commentRegDto, Long principalId);

    void modify(CommentUpdateDto commentUpdateDto, Long principalId);

    Long remove(Long commentId);

}
