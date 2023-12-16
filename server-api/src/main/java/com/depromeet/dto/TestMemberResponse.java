package com.depromeet.dto;

import com.depromeet.entity.TestMember;
import com.depromeet.entity.TestMemberStatus;
import com.depromeet.entity.TestSex;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class TestMemberResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final TestMemberStatus status;
    private final TestSex sex;
    private final int age;

    public TestMemberResponse(Long id, String email, String name, TestMemberStatus status, TestSex sex, int age) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.status = status;
        this.sex = sex;
        this.age = age;
    }
}
