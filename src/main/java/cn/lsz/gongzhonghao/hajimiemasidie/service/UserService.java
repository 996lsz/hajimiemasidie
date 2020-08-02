package cn.lsz.gongzhonghao.hajimiemasidie.service;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.User;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.WxBaseEvent;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.WxTextResponse;
import cn.lsz.gongzhonghao.hajimiemasidie.util.LogHelper;
import cn.lsz.gongzhonghao.hajimiemasidie.util.MenuUtils;
import cn.lsz.gongzhonghao.hajimiemasidie.util.XmlBeanUtils;
import core.service.MyBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant.REDIS_CONTEXT_KEY;

/**
 * description
 * 
 * @author LSZ 2020/02/12 14:44
 * @contact 648748030@qq.com
 */
@Service
public class UserService extends MyBaseService<User> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "redisTemplate" )
    private RedisTemplate redisTemplate;

    public String subscribeEvent(WxBaseEvent event){
        LogHelper.info("新关注用户:"+event.getFromUserName());
        try {
            mapper.insert(new User(event.getFromUserName(), new Date(event.getCreateTime() * 1000)));
        }catch (DuplicateKeyException e){
            LOGGER.error("重复关注用户："+event.getFromUserName());
        }
        WxTextResponse response = new WxTextResponse("哈吉咩马斯跌！欢迎关注公众号\n"+ MenuUtils.mainMenuStr(),event.getFromUserName());
        return XmlBeanUtils.toXml(response);
    }


    /*
     * 变更用户上下文
     * */
    public void setUserContext(String userName, String contextKey){
        String cacheContextKey = String.format(REDIS_CONTEXT_KEY, userName);
        redisTemplate.opsForValue().set(cacheContextKey, contextKey);
    }

    /*
     * 变更用户上下文
     * */
    public void setUserContext(String userName, String contextKey, long l, TimeUnit timeUnit){
        String cacheContextKey = String.format(REDIS_CONTEXT_KEY, userName);
        redisTemplate.opsForValue().set(cacheContextKey, contextKey, l, timeUnit);
    }

    /*
     * 获取用户上下文
     * */
    public String getUserContext(String userName){
        String contextKey = String.format(REDIS_CONTEXT_KEY, userName);
        String event = (String) redisTemplate.opsForValue().get(contextKey);
        return event;
    }

    /*
     * 删除用户上下文
     * */
    public void deleteUserContext(String userName){
        redisTemplate.delete(String.format(REDIS_CONTEXT_KEY, userName));
    }
}
