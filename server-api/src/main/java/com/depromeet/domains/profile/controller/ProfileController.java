package com.depromeet.domains.profile.controller;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.profile.dto.request.ProfileImageUrlRequest;
import com.depromeet.domains.profile.dto.request.ProfileNicknameRequest;
import com.depromeet.domains.profile.dto.response.ProfileFeedResponse;
import com.depromeet.domains.profile.dto.response.ProfileResponse;
import com.depromeet.domains.profile.service.ProfileService;
import com.depromeet.domains.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public CustomResponseEntity<ProfileResponse> getProfile(@AuthUser User user,
                                                            @PathVariable("userId") Long userId) {
        return CustomResponseEntity.success(
                this.profileService.getProfile(user, userId));
    }

    @GetMapping("/{userId}/feeds")
    public CustomResponseEntity<Slice<ProfileFeedResponse>> getProfileFeed(@AuthUser User user,
                                                                           @PathVariable("userId") Long userId,
                                                                           @RequestParam(value = "lastIdxId") Long lastIdxId,
                                                                           @RequestParam(value = "size") Integer size) {
        return CustomResponseEntity.success(
                this.profileService.getProfileFeed(user, userId, lastIdxId, size));
    }

    @PutMapping("/{userId}/nickname")
    public CustomResponseEntity<Void> updateProfileNickname(@AuthUser User user,
                                                            @PathVariable("userId") Long userId,
                                                            @RequestBody @Valid ProfileNicknameRequest nicknameRequest) {
        profileService.updateProfileNickname(user, userId, nicknameRequest.getNickname());
        return CustomResponseEntity.success();
    }

    @PutMapping("/{userId}/img")
    public CustomResponseEntity<Void> updateProfileImageUrl(@AuthUser User user,
                                                            @PathVariable("userId") Long userId,
                                                            @RequestBody @Valid ProfileImageUrlRequest profileImageUrlRequest) {
        profileService.updateProfileImageUrl(user, userId, profileImageUrlRequest.getProfileImageUrl());
        return CustomResponseEntity.success();
    }
}
