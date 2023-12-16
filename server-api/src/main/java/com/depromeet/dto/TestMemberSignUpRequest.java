package com.depromeet.dto;

import com.depromeet.entity.TestMember;
import com.depromeet.entity.TestMemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestMemberSignUpRequest {

    private String email;

    private int age;

    private TestMemberStatus status;

    public TestMember toEntity(){
        return new TestMember(email,age,status);
    }

    public static TestMemberSignUpRequest of(String email, int age){
        return TestMemberSignUpRequest.builder()
                .email(email)
                .age(age)
                .build();
    }
}