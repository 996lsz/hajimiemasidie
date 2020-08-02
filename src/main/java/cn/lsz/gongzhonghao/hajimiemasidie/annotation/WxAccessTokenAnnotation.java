package cn.lsz.gongzhonghao.hajimiemasidie.annotation;

import java.lang.annotation.*;

/**
 * description
 *
 * @author LSZ 2019/10/15 15:24
 * @contact 648748030@qq.com
 * @desc 使用该注解的接口表明是需要获取token的，如果接口报错token失效将自动重新获取token并重调方法
 */
@Inherited
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WxAccessTokenAnnotation {

}
