package com.depromeet.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class CursorPagingCommon {

    public static <T> Slice<T> getSlice(List<?> responses, Integer size) {

        boolean hasNext = false;
        if (responses.size() > size) {
            // 마지막 항목 제거하여 다음 페이지 존재 여부 확인
            responses.remove(responses.size() - 1);
            hasNext = true;
        }

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, size);

        // SliceImpl 객체로 반환
        return (Slice<T>)  new SliceImpl<>(responses, pageable, hasNext);
    }
}