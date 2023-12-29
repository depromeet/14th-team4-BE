package com.depromeet.document;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// enum 팝업 작업을 클래스로 정의
public interface DocumentLinkGenerator {

    static String generateLinkCode(DocUrl docUrl) {
        return String.format("link:common/%s.html[%s %s,role=\"popup\"]", docUrl.pageId, docUrl.text, "코드");
    }

    static String generateText(DocUrl docUrl) {
        return String.format("%s %s", docUrl.text, "코드명");
    }

    @RequiredArgsConstructor
    enum DocUrl {// 아래에 모든 enum에 대해서 정의하면 됨
        TEST_ENUM_TYPE("test-enum", "테스트 enum")
        ;

        private final String pageId;
        @Getter
        private final String text;
    }
}
