package com.depromeet.domains.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileImageUrlRequest {
    @NotBlank
    private String profileImageUrl;

    public ProfileImageUrlRequest(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}
