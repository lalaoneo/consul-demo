package com.lori.consul.provider.api;


@RequestMapping("/provider")
public interface HelloWord {

    @RequestMapping("/hello")
    String hello();
}
