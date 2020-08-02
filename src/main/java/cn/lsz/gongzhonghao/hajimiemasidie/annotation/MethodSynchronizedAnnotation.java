package cn.lsz.gongzhonghao.hajimiemasidie.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 用于缓存,目前key
 *
 * @author LSZ 2019/12/11 10:03:00
 * @contact 648748030@qq.com
 */
@Inherited
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodSynchronizedAnnotation {

    String redisKey();

    String[] keys();

    long timeOut() default 30;

    TimeUnit timeUnit() default TimeUnit.MINUTES;
}
