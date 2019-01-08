package com.lori.consul.consumer.feign.client;

import com.lori.consul.provider.api.HelloWord;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("consul-provider")
public interface HelloWordClient extends HelloWord {
}
