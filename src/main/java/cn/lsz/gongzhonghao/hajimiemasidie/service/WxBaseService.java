package cn.lsz.gongzhonghao.hajimiemasidie.service;

import cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.WxToken;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import static cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant.*;

/**
 * description
 * 
 * @author LSZ 2020/02/12 14:44
 * @contact 648748030@qq.com
 */
@Service
public class WxBaseService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WxService wxService;

    public WxToken getAccessToken() {
        String url = String.format(TOKEN_URL, AppConstant.getAppId(), AppConstant.getAppSecrect());
        WxToken token = restTemplate.getForObject(url,WxToken.class);
        return token;

    }

}
