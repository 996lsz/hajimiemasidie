package cn.lsz.gongzhonghao.hajimiemasidie.config;

import cn.lsz.gongzhonghao.hajimiemasidie.interceptor.WxInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author LSZ 2020/05/22 14:15
 * @contact 648748030@qq.com
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
	private WxInterceptor wxInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(wxInterceptor).addPathPatterns("/wx/**");
	}


}