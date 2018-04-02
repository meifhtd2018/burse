package com.hnair.consumer.wallet.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Using IntelliJ IDEA.
 *
 * @author HNAyd.xian
 * @date 2018/2/2 16:25
 */
@RestController
public class TestRestController {

    @RequestMapping("/strTest/{str}")
    public String stringTest(@PathVariable String str) {
        return str;
    }
}
