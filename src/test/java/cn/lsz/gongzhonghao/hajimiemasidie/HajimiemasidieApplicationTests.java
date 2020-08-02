package cn.lsz.gongzhonghao.hajimiemasidie;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.Chengyu;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.WxToken;
import cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu.ChengyuService;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant.TOKEN_URL;


@SpringBootTest
public class HajimiemasidieApplicationTests {

    @Autowired
    private ChengyuService service;

    @Resource(name = "redisTemplate" )
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test(){
        //service.selectOne(new Chengyu("乐不思蜀"));
    }

    @Test
    public void testToken(){
        String url = String.format(TOKEN_URL, "", "");
        WxToken token = restTemplate.getForObject(url, WxToken.class);
        System.out.println(token);
    }

}
