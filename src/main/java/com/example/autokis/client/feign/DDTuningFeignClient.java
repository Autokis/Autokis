package com.example.autokis.client.feign;

import com.example.autokis.openapi.model.DDResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ddtuning", url = "https://ddaudio.com.ua/api")
@Headers("Authorization: {token}")
public interface DDTuningFeignClient {
    @GetMapping("/price/retail?offset={offset}&lang=ua&limit={limit}")
    DDResponse getAllProducts(@PathVariable int offset, @RequestHeader("Authorization") String token, @PathVariable("limit") int limit);
}
