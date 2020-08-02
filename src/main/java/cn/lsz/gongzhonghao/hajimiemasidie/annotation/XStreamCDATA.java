package cn.lsz.gongzhonghao.hajimiemasidie.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description
 * 
 * @author LSZ 2020/02/12 17:12
 * @contact 648748030@qq.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface XStreamCDATA {

}
