package cn.lsz.gongzhonghao.hajimiemasidie.aspect;

import cn.lsz.gongzhonghao.hajimiemasidie.annotation.LogAnnotation;
import cn.lsz.gongzhonghao.hajimiemasidie.util.LogHelper;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * description
 * 
 * @author LSZ 2019/10/15 15:29
 * @contact 648748030@qq.com
 */
@Aspect
@Component
public class LogAspect {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Around("@annotation(logAnnotation)")
    public Object aroundLog(ProceedingJoinPoint joinpoint, LogAnnotation logAnnotation) throws Throwable {
        Object result = null;
        boolean success = true;
        Throwable e = null;
        StringBuilder log = new StringBuilder();
        try {
            result = joinpoint.proceed();
        }catch (Throwable tempe){
            success = false;
            e = tempe;
            throw e;
        }finally {
            //成功日志
            log.append(logAnnotation.finallyLog()).append("执行是否成功:").append(success).append("\n");
            //参数日志
            log.append(argsLog(joinpoint.getArgs()));
            //过程日志
            log.append(logHelperLog());
            //结果日志
            log.append(resultLog(result));

            if(success){
                LOGGER.info(log.toString());
            }else {
                LOGGER.error(log.toString(), e);
            }
            LogHelper.remove();
        }
        return result;
    }

    private String logHelperLog(){
        StringBuilder log  = new StringBuilder();
        List<LogHelper.Item> itemList = LogHelper.getLog();
        if(itemList != null){
            log.append("过程：\n");
            for (LogHelper.Item item : itemList) {
                String s = item.getLogStr();
                Object[] o = item.getArgs();
                Throwable throwable = item.getThrowable();
                switch (item.getLevel()){
                    /*case DEBUG:{
                        if(o != null){
                            LOGGER.debug(s, o);
                        }else if(item.getThrowable() != null){
                            LOGGER.debug(s, throwable);
                        }else{
                            LOGGER.debug(s);
                        }
                        break;
                    }*/
                    case INFO:{
                        if(o != null){
                            log.append(MessageFormatter.arrayFormat(s, o).getMessage());
                        }else if(item.getThrowable() != null){
                            //LOGGER.info(s, throwable);
                        }else{
                            log.append(s);
                        }
                        log.append("\n");
                        break;
                    }
                    /*case WARN:{
                        if(o != null){
                            LOGGER.warn(s, o);
                        }else if(item.getThrowable() != null){
                            LOGGER.warn(s, throwable);
                        }else{
                            LOGGER.warn(s);
                        }
                        break;
                    }
                    case ERROR:{
                        if(o != null){
                            LOGGER.error(s, o);
                        }else if(item.getThrowable() != null){
                            LOGGER.error(s, throwable);
                        }else{
                            LOGGER.error(s);
                        }
                        break;
                    }*/
                    default:{}
                }
            }
        }
        return log.toString();
    }

    private String resultLog(Object result){
        StringBuilder log = new StringBuilder();
        //如果有其它类型参数不能正常展示继续完善
        if(result != null) {
            log.append("执行结果:");
            if(result instanceof AbstractMap){
                Map map = (Map) result;
                log.append(mapLog(map)).append("\n");
            } else {
                log.append(result.toString()).append("\n");
            }
        }
        return log.toString();
    }

    private String argsLog(Object args[]){
        StringBuilder log = new StringBuilder("入参：\n");
        int argIndex = 1;
        for (Object arg : args) {
            log.append("参数").append(argIndex).append(":");
            //如果有其它类型参数不能正常展示继续完善
            if(arg instanceof AbstractMap){
                Map map = (Map) arg;
                log.append(mapLog(map)).append("\n");
            }else{
                log.append(arg.toString()).append("\n");
            }

            argIndex++;
        }
        return log.toString();
    }

    private String mapLog(Map map){
        JSONObject json = new JSONObject(map);
        return json.toJSONString();
    }

}
