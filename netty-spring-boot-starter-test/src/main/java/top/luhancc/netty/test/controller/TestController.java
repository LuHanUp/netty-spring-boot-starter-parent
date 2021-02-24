package top.luhancc.netty.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luHan
 * @create 2021/2/24 11:25
 * @since 1.0.0
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test(@RequestParam("name") String name) {
        return "hello spring boot netty name=" + name;
    }

    @RequestMapping("/test_json")
    public Map<String, String> testJson() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "脏水那");
        return map;
    }
}
