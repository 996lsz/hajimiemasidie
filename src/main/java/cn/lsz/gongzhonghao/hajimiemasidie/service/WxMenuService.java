package cn.lsz.gongzhonghao.hajimiemasidie.service;

import cn.lsz.gongzhonghao.hajimiemasidie.annotation.LogAnnotation;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.*;
import cn.lsz.gongzhonghao.hajimiemasidie.util.MenuUtils;
import cn.lsz.gongzhonghao.hajimiemasidie.util.XmlBeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant.REDIS_USER_MENU_KEY;

/**
 * description
 * 
 * @author LSZ 2020/02/12 14:44
 * @contact 648748030@qq.com
 */
@Service
public class WxMenuService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "redisTemplate" )
    private RedisTemplate redisTemplate;

    @Resource(name = "sourceRedisTemplate" )
    private RedisTemplate sourceRedisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext applicationContext;

    private String[] menuNum = {"0","1","2","3","4","5","6","7","8","9","#"};

    @LogAnnotation(finallyLog = "用户请求")
    public String mainMenu(String requestXml) {
        WxBase base = (WxBase) XmlBeanUtils.transform(requestXml, WxBase.class);
        String formUser = base.getFromUserName();
        String msgType = base.getMsgType();
        switch (msgType) {
            case "text" :{
                WxTextRequest textRequest = (WxTextRequest) XmlBeanUtils.transform(requestXml, WxTextRequest.class);
                String event = userService.getUserContext(formUser);
                //如果当前有上下文，则先执行上下文逻辑
                if(event != null){
                    return dealEvent(event, formUser, textRequest.getContent());
                }
                if(ArrayUtils.contains(menuNum,textRequest.getContent())){
                    //处理菜单请求
                    return dealMenu(textRequest);
                }
                break;
            }case "event" :{
                WxBaseEvent event = (WxBaseEvent) XmlBeanUtils.transform(requestXml, WxBaseEvent.class);
                //关注事件
                if("subscribe".equals(event.getEvent())){
                    return userService.subscribeEvent(event);
                }
                break;
            } default:{

            }
        }
        return null;
    }

    private String dealMenu(WxTextRequest textRequest) {
        String userName = textRequest.getFromUserName();
        String key = textRequest.getContent();
        String event = null;
        String[] concurrentMenu = getUserMenuCache(userName);
        //用户第一次进入菜单或者输入#
        if(concurrentMenu == null || concurrentMenu.length == 0 || "#".equals(key)){
            setUserMenuCache(userName, new String[]{"#"});
            return XmlBeanUtils.toXml(new WxTextResponse(MenuUtils.mainMenuStr(),userName));
        }
        String[] nextMenu = ArrayUtils.add(concurrentMenu, key);
        Menu menu = MenuUtils.listMenu(nextMenu);
        //没有对应的菜单
        if(menu == null){
            return XmlBeanUtils.toXml(new WxTextResponse("没有对应的菜单喔",userName));
        }else{
            //如果选中的key有下级菜单则展示
            if(menu.getSubMenus() != null && menu.getSubMenus().size() > 0){
                setUserMenuCache(userName, nextMenu);
                return XmlBeanUtils.toXml(new WxTextResponse(MenuUtils.menusStr(menu.getSubMenus()),userName));
            }
            //如果是功能，则执行对应的逻辑
            event = menu.getEvent();
            //如果是返回，列出上级菜单
            if("back".equals(event)){
                //用户输入返回上一级菜单
                if(concurrentMenu == null || concurrentMenu.length <= 2){
                    setUserMenuCache(userName, new String[]{"#"});
                    return XmlBeanUtils.toXml(new WxTextResponse(MenuUtils.mainMenuStr(),userName));
                }else{
                    concurrentMenu = ArrayUtils.remove(concurrentMenu, concurrentMenu.length-1);
                    setUserMenuCache(userName, concurrentMenu);
                    menu = MenuUtils.listMenu(concurrentMenu);
                    return XmlBeanUtils.toXml(new WxTextResponse(MenuUtils.menusStr(menu.getSubMenus()),userName));
                }
            }
            if(StringUtils.isNotEmpty(event)){
                setUserMenuCache(userName, concurrentMenu);
                return dealEvent(event, userName, key);
            }
        }
        return null;
    }

    public String getUserMenu(String userName){
        String[] concurrentMenu = getUserMenuCache(userName);
        Menu menu = MenuUtils.listMenu(concurrentMenu);
        return XmlBeanUtils.toXml(new WxTextResponse(MenuUtils.menusStr(menu.getSubMenus()),userName));
    }

    private String dealEvent(String event, String userName, String key){
        LOGGER.info(userName + ":" + event + ":" + key);
        //执行代码块
        try {
            //methods[0]为beanName, methods[1]为methodName
            String[] methods = event.split("\\.");
            Object service = applicationContext.getBean(methods[0]);
            Method method = service.getClass().getDeclaredMethod(methods[1],String.class,String.class);
            String response = (String) method.invoke(service, userName, key);
            return response;
        } catch (Exception e) {
            LOGGER.error("dealEventError:", e);
            //e.printStackTrace();
        }
        return XmlBeanUtils.toXml(new WxTextResponse("功能尚未开放",userName));
    }

    private void setUserMenuCache(String userName, String[] keys){
        String redisKey = String.format(REDIS_USER_MENU_KEY, userName);
        sourceRedisTemplate.opsForValue().set(redisKey, keys, 3, TimeUnit.MINUTES);
    }

    private String[] getUserMenuCache(String userName){
        String redisKey = String.format(REDIS_USER_MENU_KEY, userName);
        String[] concurrentMenu = (String[]) sourceRedisTemplate.opsForValue().get(redisKey);
        return concurrentMenu;
    }
}
