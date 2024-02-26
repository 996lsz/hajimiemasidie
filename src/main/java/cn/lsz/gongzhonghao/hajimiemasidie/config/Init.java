package cn.lsz.gongzhonghao.hajimiemasidie.config;

import cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu.ChengyuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.ChengyuConstant.REDIS_CHENGYU_REFRESH_KEY;

/**
 * description
 * 
 * @author LSZ 2020/02/14 17:20
 * @contact 648748030@qq.com
 */
@Component
public class Init {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChengyuService chengyuService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @PostConstruct
    public void init() {
        //初始化成语接龙缓存
        if(redisTemplate.opsForValue().setIfAbsent(REDIS_CHENGYU_REFRESH_KEY,1)){
            LOGGER.info("初始化成语接龙缓存");
            try {
                chengyuService.init();
            }catch (Exception e){
                redisTemplate.delete(REDIS_CHENGYU_REFRESH_KEY);
                throw e;
            }
            LOGGER.info("初始化成语接龙缓存完成");
        };
    }

}
