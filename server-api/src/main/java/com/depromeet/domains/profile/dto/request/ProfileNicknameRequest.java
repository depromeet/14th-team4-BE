package com.depromeet.domains.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileNicknameRequest {
    @NotBlank
    @Size(min = 1, max = 8)
    private String nickname;

    public ProfileNicknameRequest(String nickname) {
        this.nickname = nickname;
    }
}
