package com.depromeet.domains.comment.repository;

import com.depromeet.domains.comment.dto.response.CommentsResponse;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentsResponse> findByFeedId(Long feedId);
}
