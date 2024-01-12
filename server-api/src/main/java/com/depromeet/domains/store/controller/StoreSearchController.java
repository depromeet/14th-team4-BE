package com.depromeet.domains.store.controller;

import com.depromeet.annotation.AuthUser;
import com.depromeet.common.exception.CustomResponseEntity;
import com.depromeet.domains.store.dto.response.StoreSearchResponse;
import com.depromeet.domains.store.service.StoreSearchService;
import com.depromeet.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Controller
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreSearchController {

    private final StoreSearchService storeSearchService;

    @GetMapping("/search")
    public CustomResponseEntity<List<StoreSearchResponse>> search(@AuthUser User user,
                                                                  @RequestParam("query") String query,
                                                                  @RequestParam("x") String x,
                                                                  @RequestParam("y") String y,
                                                                  @RequestParam("storePage") Optional<Integer> storePage,
                                                                  @RequestParam("cafePage") Optional<Integer> cafePage
    ) {
        return CustomResponseEntity.success(storeSearchService.searchStoreList(user, query, x, y, storePage, cafePage));
    }
}
