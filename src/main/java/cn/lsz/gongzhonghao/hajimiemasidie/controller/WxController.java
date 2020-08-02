package cn.lsz.gongzhonghao.hajimiemasidie.controller;

import cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu.ChengyuService;
import cn.lsz.gongzhonghao.hajimiemasidie.service.WxMenuService;
import cn.lsz.gongzhonghao.hajimiemasidie.service.WxService;
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
@RequestMapping({"/wx"})
public class WxController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxService wxService;

    @Autowired
    private WxMenuService wxMenuService;

    @Autowired
    private ChengyuService chengyuService;

    /**
     * 验证微信加密验证
     * @param signature
     * @param echostr
     * @param timestamp
     * @param nonce
     * @return
     */
    @GetMapping("")
    public String connectionTest(String signature, String echostr, String timestamp, String nonce) {
        LOGGER.info("signature:"+signature);
        LOGGER.info("echostr:"+echostr);
        LOGGER.info("timestamp:"+timestamp);
        LOGGER.info("nonce:"+nonce);
        String mySignature = SignUtils.sha1(timestamp, nonce);
        LOGGER.info("mySignature:"+mySignature);
        if(signature.equals(mySignature)){
            return echostr;
        }
        return "failed";
    }

    @PostMapping(value = "")
    @ResponseBody
    public String mainMenu(@RequestBody String requestXml) throws IOException {
        String responseXml = wxMenuService.mainMenu(requestXml);
        return StringUtils.defaultString(responseXml,"success");
    }


}
