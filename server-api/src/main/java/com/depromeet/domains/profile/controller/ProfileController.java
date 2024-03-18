package com.depromeet.domains.profile.controller;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.profile.dto.response.ProfileResponse;
import com.depromeet.domains.profile.service.ProfileService;
import com.depromeet.domains.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public CustomResponseEntity<ProfileResponse> getProfileInfo(@PathVariable("userId") Long userId,
                                                                @AuthUser User user) {
        return CustomResponseEntity.success(
                this.profileService.getProfileInfo(user, user.getUserId().equals(userId)));
    }
}
