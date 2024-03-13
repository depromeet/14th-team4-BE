package com.depromeet.domains.feed.repository;

import java.util.List;

import com.depromeet.domains.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {

	List<Feed> findByUserId(Long userId);

}
