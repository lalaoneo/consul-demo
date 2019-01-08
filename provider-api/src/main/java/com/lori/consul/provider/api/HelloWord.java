package com.lori.consul.provider.api;


import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/provider")
public interface HelloWord {

    @RequestMapping("/hello")
    String hello();
}
