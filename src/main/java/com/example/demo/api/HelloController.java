package com.example.demo.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/3/23
 */
@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloController {
    @GetMapping("/say/hello")
    public String sayHello() {
        log.info("say hello world");
        return "Hello World";
    }
}
