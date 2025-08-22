package com.boardify.board_service.feign;

import com.boardify.board_service.dto.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "PROFILE-SERVICE",
        configuration = FeignClientConfig.class
)
public interface ProfileInterface {
    @GetMapping("/profile/isUserValid/{user_id}")
    ResponseWrapper<Boolean> isUserValid(@PathVariable("user_id") Long userId);
}
