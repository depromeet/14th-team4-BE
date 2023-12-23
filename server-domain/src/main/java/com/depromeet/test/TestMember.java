package com.depromeet.test;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TestMember {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String email;

    private int age;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private TestMemberStatus status;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private TestSex sex;

    public TestMember(final String email,
                  final int age,
                  final TestMemberStatus status) {
        this.email = email;
        this.age = age;
        this.status = status;
        this.sex = TestSex.MALE;
    }
}