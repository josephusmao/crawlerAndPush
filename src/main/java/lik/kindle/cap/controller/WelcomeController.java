package lik.kindle.cap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author langyi
 * @date 2017/1/27.
 */
@Controller
@RequestMapping("")
public class WelcomeController {
    private static Logger log = LoggerFactory.getLogger(WelcomeController.class);

//    @Autowired
//    private HttpClientService httpClientService;

    @RequestMapping("index.html")
    public String index(){
        log.error(".....121");
        return "view/index";
    }
}
