package com.depromeet.domains.commet.controller;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.comment.dto.request.CommentCreateRequest;
import com.depromeet.domains.comment.dto.request.CommentUpdateRequest;
import com.depromeet.domains.comment.dto.response.CommentsResponse;
import com.depromeet.domains.commet.servicce.CommentService;
import com.depromeet.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class CommentController {

    private final CommentService commentService;

    // 피드의 댓글 전체 조회
    @GetMapping("/feeds/{feedId}/comments")
    public CustomResponseEntity<List<CommentsResponse>> getComments(@PathVariable("feedId") Long feedId) {
        return CustomResponseEntity.success(commentService.getComments(feedId));
    }

    // 피드에 댓글 작성
    @PostMapping("/comments")
    public CustomResponseEntity<Void> createComment(@AuthUser User user, @RequestBody CommentCreateRequest request) {
        commentService.createComment(user, request);
        return CustomResponseEntity.success();
    }

    // 피드의 댓글 수정
    @PutMapping("/comments/{commentId}")
    public CustomResponseEntity<Void> updateComment(@AuthUser User user, @RequestBody CommentUpdateRequest request,
                                                    @PathVariable("commentId") Long commentId) {
        commentService.updateComment(user, request, commentId);
        return CustomResponseEntity.success();
    }

    // 피드의 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public CustomResponseEntity<Void> deleteComment(@AuthUser User user, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(user, commentId);
        return CustomResponseEntity.success();
    }
}
