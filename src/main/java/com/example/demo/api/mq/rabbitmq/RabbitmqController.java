package com.example.demo.api.mq.rabbitmq;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/7/19
 */
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {

    @Resource
    private Sender sender;

    @Resource
    private Receiver receiver;

    @GetMapping("/send")
    public ResponseEntity<String> send() throws IOException, TimeoutException {
        sender.send();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/start/receiving")
    public ResponseEntity<String> startReceiving() throws IOException, InterruptedException, TimeoutException {
        receiver.start();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
