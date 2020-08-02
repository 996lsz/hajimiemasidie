package cn.lsz.gongzhonghao.hajimiemasidie.aspect;

import cn.lsz.gongzhonghao.hajimiemasidie.annotation.MethodSynchronizedAnnotation;
import cn.lsz.gongzhonghao.hajimiemasidie.config.MethodSynchronizedConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import core.exception.MethodSynchronizedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定义注解@RedisCache处理
 * 目前只支持参数为String(基本类型)...,opsForValue的缓存
 * 
 * @author LSZ 2019/10/15 15:29
 * @contact 648748030@qq.com
 */
@Aspect
@Component
public class MethodSynchronizedAspect {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplate redisTemplate;

    @Around("@annotation(MethodSynchronizedAnnotation)")
    public Object around(ProceedingJoinPoint joinpoint, MethodSynchronizedAnnotation MethodSynchronizedAnnotation) throws Throwable {
        Object result;
        MethodSignature msig = (MethodSignature) joinpoint.getSignature();
        Method currentMethod = joinpoint.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
        List<MethodSynchronizedConfig.MyClass> list = MethodSynchronizedConfig.myMap.get(currentMethod.toString());

        String redisKey = MethodSynchronizedAnnotation.redisKey();
        Object[] args = joinpoint.getArgs();

        for (MethodSynchronizedConfig.MyClass myClass : list) {
            Object arg = args[myClass.getIndex()];
            if(myClass.getMethod() == null){
                //内容为下标，直接拼接
                redisKey = String.format(redisKey,getRedisKeyByClassType(arg));
            } else {
                for(int i = 0; i < myClass.getMethod().size(); i++) {
                    Method method = myClass.getMethod().get(i);
                    if(myClass.getParams().size() == 0){
                        arg = method.invoke(arg);
                    }else{
                        Object param = myClass.getParams().get(i);
                        if(param == null ){
                            arg = method.invoke(arg);
                        }else{
                            arg = method.invoke(arg, param);
                        }
                    }
                }
                redisKey = String.format(redisKey,getRedisKeyByClassType(arg));
           }
        }
        Object executingFlag = redisTemplate.opsForValue().get(redisKey);
        //操作频繁，终止
        if(executingFlag != null){
            throw new MethodSynchronizedException();
        }else{
            redisTemplate.opsForValue().set(redisKey,1,1, TimeUnit.MINUTES);
            result =  joinpoint.proceed();
            redisTemplate.delete(redisKey);
        }
        return result;
    }

    private Object getRedisKeyByClassType(Object arg){
        if(JSON.class.isAssignableFrom(arg.getClass())){
            return JSONObject.parseObject(JSONObject.toJSONString(arg),Map.class);
        }else{
            return arg;
        }
    }
    
}
