package cn.lsz.gongzhonghao.hajimiemasidie.controller;

import cn.lsz.gongzhonghao.hajimiemasidie.service.WxMenuService;
import cn.lsz.gongzhonghao.hajimiemasidie.util.SignUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * description
 * 
 * @author LSZ 2020/02/12 11:38
 * @contact 648748030@qq.com
 */
@RestController
@RequestMapping({"/test"})
public class TestController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxMenuService wxMenuService;

    @GetMapping("/test")
    public String connectionTest() {
        LOGGER.info("111");
        return "success";
    }

    /**
     * 可以使用这里来测试
     * @example localhost:80/test?text=1
     * @param text
     * @return
     */
    @PostMapping(value = "")
    @ResponseBody
    public String mainMenu(@RequestParam("text") String text) {

        String requestXml = "<xml>\n" +
                "  <ToUserName>123</ToUserName>\n" +
                "  <FromUserName>222</FromUserName>\n" +
                "  <CreateTime>1348831860</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA[%s]]></Content>\n" +
                "  <MsgId>1234567890123456</MsgId>\n" +
                "</xml>";

        String responseXml = wxMenuService.mainMenu(String.format(requestXml, text));
        return StringUtils.defaultString(responseXml,"success");
    }
}
