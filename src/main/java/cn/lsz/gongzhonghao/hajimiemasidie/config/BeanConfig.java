package cn.lsz.gongzhonghao.hajimiemasidie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * description
 * 
 * @author LSZ 2020/02/12 15:55
 * @contact 648748030@qq.com
 */
@Configuration
public class BeanConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
