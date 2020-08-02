package cn.lsz.gongzhonghao.hajimiemasidie.util;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.Menu;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant.MENU;

/**
 * description
 * 
 * @author LSZ 2020/02/14 22:28
 * @contact 648748030@qq.com
 */
public class MenuUtils {

    public static Menu listMenu(String[] keys){
        //Menu menu = JSON.parseObject(MENU_JSON, Menu.class);
        Menu menu = MENU;
        //WxTextResponse menu = null;
        //如果用户是首次输入，展示主菜单
        if(keys == null || keys.length == 0){
            return menu;
            //menu = new WxTextResponse(mainMenuXml(),userName);
        }else{
            for(int i = 1; i < keys.length; i++){
                 menu = getSubMenus(keys[i],menu.getSubMenus());
            }
            return menu;
        }
    }

    public static String mainMenuStr(){
        //Menu menu = JSON.parseObject(MENU_JSON, Menu.class);
        Menu menu = MENU;
        StringBuilder menuList = new StringBuilder("主菜单：\n");
        for(Menu subMenu : menu.getSubMenus()){
            menuList.append(subMenu.getKey()).append(" ").append(subMenu.getTitle()).append("\n");
        }
        menuList.append("\n").append("输入神秘编号即可解锁新世界，0返回上一层，#返回主菜单");
        return menuList.toString();
    }

    public static String menusStr(List<Menu> menus){
        StringBuilder menuList = new StringBuilder("主菜单：\n");
        for(Menu subMenu : menus){
            menuList.append(subMenu.getKey()).append(" ").append(subMenu.getTitle()).append("\n");
        }
        menuList.append("\n").append("输入神秘编号即可解锁新世界，0返回上一层，#返回主菜单");
        return menuList.toString();
    }

    private static Menu getSubMenus(String key, List<Menu> menus){
        for(Menu menu : menus){
            if(key.equals(menu.getKey())){
                return menu;
            }
        }
        return null;
    }

}
