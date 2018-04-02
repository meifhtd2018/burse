package com.hnair.consumer.wallet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Using IntelliJ IDEA.
 *
 * @author HNAyd.xian
 * @date 2018/2/2 16:22
 */
@Controller
public class TestController {

    @RequestMapping("/test")
    public String testRedirect(Model model) {
        model.addAttribute("name", "zxhangjialou");
        return "test";
    }

    @RequestMapping("/testPage")
    public String testToPage(Model model) {
        return "page/mobile";
    }
}
