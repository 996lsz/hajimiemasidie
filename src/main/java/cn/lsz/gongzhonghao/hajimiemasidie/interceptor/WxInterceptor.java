package cn.lsz.gongzhonghao.hajimiemasidie.interceptor;

import cn.lsz.gongzhonghao.hajimiemasidie.util.SignUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信接口拦截器
 *
 * @author LSZ 2020/05/22 14:14
 * @contact 648748030@qq.com
 */
@Configuration
public class WxInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String mySignature = SignUtils.sha1(timestamp, nonce);
		if(signature.equals(mySignature)){
			return true;
		}
		return false;
	}


}
