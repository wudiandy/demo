package com.example.demo.api.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/4/12
 */
@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisTestController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/set")
    public String set(@RequestBody RestDto restDto) {
        String key = restDto.getKey();
        String value = restDto.getValue();
        log.debug("redis set");
        log.debug("key = " + key);
        log.debug("value = " + key);
        redisTemplate.opsForValue().set(key, value);
        return "success";
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        redisTemplate.delete(key);
        return "success";
    }

}
