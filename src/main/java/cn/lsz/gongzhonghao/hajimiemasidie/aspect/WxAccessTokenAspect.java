package cn.lsz.gongzhonghao.hajimiemasidie.aspect;

import cn.lsz.gongzhonghao.hajimiemasidie.annotation.WxAccessTokenAnnotation;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.WxToken;
import cn.lsz.gongzhonghao.hajimiemasidie.service.WxBaseService;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant.REDIS_TOKEN_KEY;


/**
 * description
 * 
 * @author LSZ 2019/10/15 15:29
 * @contact 648748030@qq.com
 */
@Aspect
@Component
public class WxAccessTokenAspect {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WxBaseService service;

    @Autowired
    RedisTemplate redisTemplate;

    @Around("@annotation(tokenAnnotation)")
    public Object around(ProceedingJoinPoint joinpoint, WxAccessTokenAnnotation tokenAnnotation) throws Throwable {
        Object result =  joinpoint.proceed();
        JSONObject jsonObject;
        if(result.getClass() == JSONObject.class){
            jsonObject = (JSONObject) result;
        }else{
            jsonObject = JSONObject.parseObject(result.toString());
        }
        Integer errCode = jsonObject.getInteger("errcode");
        //40014为token无效，0为api调用失败，具体原因errMsg
        if(errCode != null && 40014 == errCode){
            WxToken newToken = service.getAccessToken();
            String token = newToken.getAccessToken();
            //有效时间（秒）
            int expiresIn = newToken.getExpiresIn();
            redisTemplate.opsForValue().set(REDIS_TOKEN_KEY,token,expiresIn, TimeUnit.SECONDS);
            joinpoint.proceed();
        }
        return result;
    }



    
}
