package com.depromeet.domains.commet.servicce;

import com.depromeet.common.exception.CustomException;
import com.depromeet.common.exception.Result;
import com.depromeet.domains.comment.dto.request.CommentCreateRequest;
import com.depromeet.domains.comment.dto.request.CommentUpdateRequest;
import com.depromeet.domains.comment.dto.response.CommentsResponse;
import com.depromeet.domains.comment.entity.Comment;
import com.depromeet.domains.comment.repository.CommentRepository;
import com.depromeet.domains.user.entity.User;
import com.depromeet.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CommentsResponse> getComments(Long feedId) {
        return commentRepository.findByFeedId(feedId);
    }

    @Transactional
    public Void createComment(User user, CommentCreateRequest request) {
        User findUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new CustomException(Result.NOT_FOUND_USER));

        Comment comment = Comment.builder()
                .feedId(request.getFeedId())
                .userId(findUser.getUserId())
                .description(request.getDescription())
                .build();

        commentRepository.save(comment);

        return null;
    }

    @Transactional
    public Void updateComment(User user, CommentUpdateRequest request, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(Result.NOT_FOUND_COMMENT));

        if (!comment.getUserId().equals(user.getUserId())) {
            throw new CustomException(Result.UNAUTHORIZED_USER);
        }

        comment.updateDescription(request.getDescription());

        return null;
    }

    @Transactional
    public Void deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(Result.NOT_FOUND_COMMENT));

        if (!comment.getUserId().equals(user.getUserId())) {
            throw new CustomException(Result.UNAUTHORIZED_USER);
        }
        commentRepository.deleteById(commentId);
        return null;
    }
}
