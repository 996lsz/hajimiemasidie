package cn.lsz.gongzhonghao.hajimiemasidie.service;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.WxToken;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant.*;

/**
 * description
 * 
 * @author LSZ 2020/02/12 14:44
 * @contact 648748030@qq.com
 */
@Service
public class WxService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WxBaseService wxBaseService;

    public String fetchAccessToken() {
        String token = (String) redisTemplate.opsForValue().get(REDIS_TOKEN_KEY);
        if(token == null){
            WxToken newToken = wxBaseService.getAccessToken();
            token = newToken.getAccessToken();
            //有效时间（秒）
            int expiresIn = newToken.getExpiresIn();
            redisTemplate.opsForValue().set(REDIS_TOKEN_KEY,token,expiresIn, TimeUnit.SECONDS);
        }
        return token;
    }


}
