package com.depromeet.document;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnumDocs {
//     문서화하고 싶은 모든 enum값을 명시(아래는 예시)
    Map<String,String> testSex;
    Map<String,String> testMemberStatus;
}