package com.depromeet.controller;

import com.depromeet.dto.TestMemberResponse;
import com.depromeet.dto.TestMemberSignUpRequest;
import com.depromeet.entity.TestMemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class TestMemberController {

    @GetMapping("/{id}")
    public TestMemberResponse getMember(@PathVariable Long id) {
        return TestMemberResponse.builder().id(1L).email("example@naver.com").name("a").build();
    }

    @PostMapping
    public TestMemberResponse createMember(@RequestBody TestMemberSignUpRequest dto){
        return TestMemberResponse.builder().age(1).email("example@naver.com").status(TestMemberStatus.NORMAL).build();
    }
}
