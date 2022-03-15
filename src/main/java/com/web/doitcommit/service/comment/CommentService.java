package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.dto.comment.CommentUpdateDto;
import com.web.doitcommit.dto.memberTag.MemberTagResDto;

import java.util.List;

public interface CommentService {

    Comment register(CommentRegDto commentRegDto, Long principalId);

    void modify(CommentUpdateDto commentUpdateDto);

    Long remove(Long commentId);

    List<MemberTagResDto> getMemberTagList(Long boardId);

}
