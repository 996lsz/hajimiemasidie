package cn.lsz.gongzhonghao.hajimiemasidie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(value = "cn.lsz.gongzhonghao.hajimiemasidie.mapper")
@EnableAspectJAutoProxy(proxyTargetClass=true, exposeProxy = true)
@SpringBootApplication
public class HajimiemasidieApplication {

    public static void main(String[] args) {
        SpringApplication.run(HajimiemasidieApplication.class, args);
    }

}
