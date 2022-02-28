package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.dto.comment.CommentRegDto;

public interface CommentService {

    Comment createComment(CommentRegDto commentRegDto, Long principalId);

}
