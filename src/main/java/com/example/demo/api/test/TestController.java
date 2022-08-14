package com.example.demo.api.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/2
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @PostMapping("/getWandaData")
    public String getData(@RequestBody WandaParam wandaParam) throws InterruptedException {
        log.info("获取重点人群数据");
        log.info("参数：{}", wandaParam.toString());
        Thread.sleep(3000);
        String searchParam = wandaParam.getSearchParam();
        String cardNum = searchParam.substring(11, searchParam.length() - 2);
        String data;
        final String card1 = "360105198803134041";
        final String card2 = "231201197303154078";
        switch (cardNum) {
            case card1:
                data = "{\"code\":\"0\",\"desc\":\"成功\",\"dataInfo\":{\"pageInfo\":{\"page\":1,\"pageSize\":10,\"totals\":1},\"data\":[{\"XM\":\"余建飞\",\"GXYZT\":0,\"TNBZT\":1,\"ZJHM\":\"" + cardNum + "\"}]}}";
                break;
            case card2:
                data = "{\"code\":\"0\",\"desc\":\"成功\",\"dataInfo\":{\"pageInfo\":{\"page\":1,\"pageSize\":10,\"totals\":1},\"data\":[{\"XM\":\"余建飞\",\"GXYZT\":1,\"TNBZT\":0,\"ZJHM\":\"" + cardNum + "\"}]}}";
                break;
            default:
                data = "{\"code\":\"0\",\"desc\":\"成功\",\"dataInfo\":{\"pageInfo\":{\"page\":1,\"pageSize\":10,\"totals\":1},\"data\":[{\"XM\":\"余建飞\",\"GXYZT\":1,\"TNBZT\":1,\"ZJHM\":\"" + cardNum + "\"}]}}";
                break;
        }
        // {"code":"0","desc":"成功","dataInfo":{"pageInfo":{"page":1,"pageSize":10,"totals":1},"data":[{"XM":"余建飞","GXYZT":1,"TNBZT":1,"ZJHM":""}]}}
        return data;
    }

    public static void main(String[] args) {
        String str = " and zjhm='211111111111111112' ";
        System.out.println(str.substring(11, str.length() - 2));
    }
}
