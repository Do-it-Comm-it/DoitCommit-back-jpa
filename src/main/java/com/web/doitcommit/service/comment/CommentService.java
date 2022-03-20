package com.web.doitcommit.service.comment;

import com.web.doitcommit.domain.comment.Comment;
import com.web.doitcommit.dto.comment.CommentListDto;
import com.web.doitcommit.dto.comment.CommentRegDto;
import com.web.doitcommit.dto.comment.CommentResDto;
import com.web.doitcommit.dto.comment.CommentUpdateDto;
import com.web.doitcommit.dto.memberTag.MemberTagResDto;
import com.web.doitcommit.dto.page.PageRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    Comment register(CommentRegDto commentRegDto, Long principalId);

    void modify(CommentUpdateDto commentUpdateDto);

    Long remove(Long commentId);

    CommentListDto getCommentList(Long boardId, PageRequestDto pageRequestDto);

    List<MemberTagResDto> getMemberTagList(Long boardId);

}
