package com.example.autokis.client.feign;

import com.example.autokis.openapi.model.DDResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "ddtuning", url = "https://ddaudio.com.ua/api")
@Headers("Authorization: {token}")
public interface DDTuningFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/price/retail?offset={offset}&lang=ua")
    DDResponse getAllProducts(@PathVariable int offset, @RequestHeader("Authorization") String token);
}
