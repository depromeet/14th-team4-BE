package com.depromeet.domains.comment.repository;

import com.depromeet.domains.comment.dto.response.CommentsResponse;
import com.depromeet.domains.comment.entity.QComment;
import com.depromeet.domains.user.entity.QUser;
import com.querydsl.core.types.Projections;
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
    public List<CommentsResponse> findByFeedId(Long feedId) {
        return jpaQueryFactory.select(Projections.fields(CommentsResponse.class,
                        user.userId,
                        user.profileImageUrl,
                        user.nickname,
                        comment.description,
                        comment.createdAt,
                        comment.updatedAt))
                .from(comment)
                .join(user).on(comment.userId.eq(user.userId))
                .where(comment.feedId.eq(feedId))
                .fetch();
    }
}
