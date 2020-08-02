package cn.lsz.gongzhonghao.hajimiemasidie.service;

import org.springframework.aop.framework.AopContext;

public interface ProxySelf<T>{

    default T self(){
        return (T) AopContext.currentProxy();
    }

}
