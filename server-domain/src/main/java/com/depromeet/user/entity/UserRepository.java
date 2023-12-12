package com.depromeet.user.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.depromeet.user.enums.SocialType;

@Repository
public class UserRepository {
	private final Map<String, User> userMap = new HashMap<>();

	public User save(User user) {
		userMap.put(user.getId(), user);
		return user;
	}

	public User findByUserId(String id) {
		return userMap.get(id);
	}

	public Optional<User> findBySocialTypeAndEmail(SocialType socialType, String email) {
		return userMap.values().stream()
			.filter(user -> socialType.equals(user.getSocialType()) && email.equals(user.getemail()))
			.findFirst();
	}
}
