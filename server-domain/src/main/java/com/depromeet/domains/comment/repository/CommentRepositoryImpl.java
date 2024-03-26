package com.depromeet.domains.comment.repository;

import com.depromeet.domains.comment.dto.response.CommentsResponse;
import com.depromeet.domains.comment.entity.QComment;
import com.depromeet.domains.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    QComment comment = QComment.comment;
    QUser user = QUser.user;

    @Override
    public List<CommentsResponse> findByFeedId(Long userId, Long feedId, Long lastIdxId, Integer size) {
        BooleanExpression isMine = JPAExpressions
                .select(comment.userId.eq(userId))
                .from(comment)
                .where(comment.feedId.eq(feedId))
                .exists();

        return jpaQueryFactory.select(Projections.constructor(CommentsResponse.class,
                        user.userId,
                        user.profileImageUrl,
                        user.nickname,
                        comment.commentId,
                        comment.description,
                        comment.createdAt,
                        comment.updatedAt,
                        isMine))
                .from(comment)
                .join(user).on(comment.userId.eq(user.userId))
                .where(comment.feedId.eq(feedId),
                        ltCommentId(lastIdxId))
                .orderBy(comment.createdAt.desc())
                .limit(size+1)
                .fetch();
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltCommentId(Long commentId) {
        if (commentId == null) {
            return null;
        }

        return comment.commentId.lt(commentId);
    }
}
