package com.example.demo.api.thread.pool;

import com.example.demo.common.result.Result;
import com.example.demo.common.result.enums.ResultStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/6/22
 */
@RequestMapping("/thread/pool")
@Controller
public class ThreadPoolController {

    @Resource
    private ThreadPoolService service;

    @GetMapping("/hot-water")
    ResponseEntity<Result> getHotWater() {
        service.getHotWater();
        Result result = new Result();
        result.setResultStatus(ResultStatus.SUCCESS);
        return ResponseEntity.ok().body(result);
    }

}
