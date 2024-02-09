package com.depromeet.domains.store.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "kakao-search-client", url = "https://dapi.kakao.com")
public interface KakaoSearchClient {

    @GetMapping("/v2/local/search/keyword.json")
    Map<String, Object> searchKeyword(@RequestHeader("Authorization") String authorization,
                                      @RequestParam("query") String query,
                                      @RequestParam("x") String x,
                                      @RequestParam("y") String y,
                                      @RequestParam("page") Integer page,
                                      @RequestParam("size") Integer size,
                                      @RequestParam("sort") String sort,
                                      @RequestParam("category_group_code") String categoryGroupCode);
}
