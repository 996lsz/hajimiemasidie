package cn.lsz.gongzhonghao.hajimiemasidie.constant;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.Menu;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description
 * 
 * @author LSZ 2020/02/12 14:24
 * @contact 648748030@qq.com
 */
@Configuration
public class AppConstant {

    //初始化菜单
    static {
        //根据空格数记录对应的父级菜单
        Map<Integer, Menu> map = new HashMap<>();
        Menu parentMenu = new Menu();
        List<Menu> nowMenuList = new ArrayList<>();
        parentMenu.setSubMenus(nowMenuList);
        //默认父级没有空格
        int lastSpace = 0;
        map.put(lastSpace, parentMenu);
        ClassPathResource classPathResource = new ClassPathResource("menu.txt");
        try(
                InputStream inputStream = classPathResource.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));) {

            String line;
            while((line=br.readLine())!=null && line.length()!=0){
                String[] split = line.split(":");
                Menu nowmMenu = new Menu(split[0].trim(), split[1]);
                if (split.length > 2) {
                    nowmMenu.setEvent(split[2].trim());
                }
                //空格数
                int nowSpace = line.replaceAll("([ ]*).*", "$1").length();
                //如果空格数相等，则属于同级目录
                if (nowSpace == lastSpace) {
                    nowMenuList.add(nowmMenu);
                } else {
                    if (nowSpace <= lastSpace) {
                        //增加对应父级目录子目录
                        map.get(nowSpace).getSubMenus().add(nowmMenu);
                    } else {
                        //新增下级目录
                        map.put(nowSpace, parentMenu);
                        nowMenuList = new ArrayList<>();
                        nowMenuList.add(nowmMenu);
                        map.get(nowSpace).setSubMenus(nowMenuList);
                    }
                }
                parentMenu = nowmMenu;
                lastSpace = nowSpace;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        MENU = map.get(0);
    }

    private static String TOKEN;
    private static String APP_ID;
    private static String APP_SECRECT;
    private static String WECHAT_ID;

    public static final String REDIS_TOKEN_KEY = "hajimiemasidie:access_token";
    public static final String REDIS_USER_MENU_KEY = "hajimiemasidie:user:%s:menu";
    public static final String REDIS_USING_FLAG_KEY = "hajimiemasidie:user:%s:using_flag:%s";
    public static final String REDIS_CONTEXT_KEY = "hajimiemasidie:user:%s:context";

    //接口
    //获取access_token
    public static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    //菜单
    public static final Menu MENU;


    @Value("${wx.token}")
    private void setToken(String token) {
        AppConstant.TOKEN = token;
    }
    @Value("${wx.app-id}")
    private void setAppId(String appId) {
        AppConstant.APP_ID = appId;
    }

    @Value("${wx.app-secrect}")
    private void setAppSecrect(String appSecrect) {
        AppConstant.APP_SECRECT = appSecrect;
    }


    @Value("${wx.wechat-id}")
    private void setWechatId(String wechatId) {
        AppConstant.WECHAT_ID = wechatId;
    }


    public static String getAppSecrect() {
        return APP_SECRECT;
    }

    public static String getToken(){
        return TOKEN;
    }

    public static String getAppId() {
        return APP_ID;
    }


    public static String getWechatId() {
        return WECHAT_ID;
    }

}
