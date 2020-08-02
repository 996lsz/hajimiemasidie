package cn.lsz.gongzhonghao.hajimiemasidie;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.Menu;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.*;

/**
 * 复古菜单测试类
 * 
 * @author LSZ 2020/03/13 13:35
 * @contact 648748030@qq.com
 */
public class MenuTest {

   /* //当用户输入数字或#视为选择菜单
    private static String[] menuNum = {"0","1","2","3","4","5","6","7","8","9","#"};
    //当前用户的菜单链
    private static String[] concurrentMenu = null;
    //主菜单
    private static Menu mainMenu;

    public static void main(String[] args) throws IOException, InterruptedException {
        String event = null;
        //初始化主菜单
        mainMenu = createMainMene();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入数字激活菜单:");
        while (true){
            String key = scanner.nextLine();
            if("break".equals(key)){
                break;
            }
            clear();
            System.out.println(dealMenu(key));
        }
    }

    static void clear() throws IOException, InterruptedException {
        //硬核清屏
        System.out.println("\n\n\n\n\n\n\n\n");
    }

    private static String dealMenu(String key){
        String event = null;
        if(ArrayUtils.contains(menuNum,key)){
            //处理菜单请求
            if(concurrentMenu == null || concurrentMenu.length == 0 || "#".equals(key)){
                concurrentMenu = new String[]{"#"};
                return mainMenuStr();
            }
            *//*else if("0".equals(key)){
                //用户输入返回上一级菜单
                if(concurrentMenu == null || concurrentMenu.length <= 2){
                    concurrentMenu = new String[]{"#"};
                    return mainMenuStr();
                }else{
                    concurrentMenu = ArrayUtils.remove(concurrentMenu, concurrentMenu.length-1);
                    Menu menu = listMenu(concurrentMenu);

                    return menusStr(menu.getSubMenus());
                }
            }*//*
            String[] nextMenu = ArrayUtils.add(concurrentMenu, key);
            Menu menu = listMenu(nextMenu);
            //没有对应的菜单
            if(menu == null){
                return "没有对应的菜单";
            }else{
                //如果选中的key有下级菜单则展示
                if(menu.getSubMenus() != null && menu.getSubMenus().size() > 0){
                    concurrentMenu = nextMenu;
                    return menusStr(menu.getSubMenus());
                }
                //如果是功能，则执行对应的逻辑
                event = menu.getEvent();
                //如果是返回，列出上级菜单
                if("back".equals(event)){
                    if(concurrentMenu == null || concurrentMenu.length <= 2){
                        concurrentMenu = new String[]{"#"};
                        return mainMenuStr();
                    }else{
                        concurrentMenu = ArrayUtils.remove(concurrentMenu, concurrentMenu.length-1);
                        menu = listMenu(concurrentMenu);
                        return menusStr(menu.getSubMenus());
                    }
                }
                if(StringUtils.isNotEmpty(event)){
                    return event;
                }
            }
        }
        return null;
    }


    *//*
     * 生成目录
     * *//*
    private static Menu createMainMene() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("menu.txt");
        File file = classPathResource.getFile();
        List<String> list = FileUtils.readLines(file);
        Menu parentMenu = new Menu();
        List<Menu> nowMenuList = new ArrayList<>();
        parentMenu.setSubMenus(nowMenuList);
        //默认父级没有空格
        int lastSpace = 0;
        Map<Integer,Menu> map = new HashMap<>();
        map.put(lastSpace,parentMenu);

        for(String line : list){
            String[] split = line.split(":");
            Menu nowmMenu = new Menu(split[0].trim(), split[1]);
            if(split.length > 2){
                nowmMenu.setEvent(split[2].trim());
            }
            //空格数
            int nowSpace = line.replaceAll("([ ]*).*", "$1").length();
            //如果空格数相等，则属于同级目录
            if(nowSpace <= lastSpace){
                map.get(nowSpace).getSubMenus().add(nowmMenu);
            }else{
                map.put(nowSpace,parentMenu);
                    //下级同级目录
                    nowMenuList = new ArrayList<>();
                    nowMenuList.add(nowmMenu);
                    map.get(nowSpace).setSubMenus(nowMenuList);
            }
            parentMenu = nowmMenu;
            lastSpace = nowSpace;
        }
        return map.get(0);
    }


    private static String mainMenuStr(){
        StringBuilder menuList = new StringBuilder("主菜单：\n");
        for(Menu subMenu : mainMenu.getSubMenus()){
            menuList.append(subMenu.getKey()).append(" ").append(subMenu.getTitle()).append("\n");
        }
        menuList.append("\n").append("请输入菜单项，0返回上一层，#返回主菜单");
        return menuList.toString();
    }

    private static String menusStr(List<Menu> menus){
        StringBuilder menuList = new StringBuilder("主菜单：\n");
        for(Menu subMenu : menus){
            menuList.append(subMenu.getKey()).append(" ").append(subMenu.getTitle()).append("\n");
        }
        menuList.append("\n").append("请输入菜单项，0返回上一层，#返回主菜单");
        return menuList.toString();
    }

    private static Menu listMenu(String[] keys){
        Menu menu = mainMenu;
        //如果用户是首次输入，展示主菜单
        if(keys == null || keys.length == 0){
            return mainMenu;
            //menu = new WxTextResponse(mainMenuXml(),userName);
        }else{
            for(int i = 1; i < keys.length; i++){
                menu = getSubMenus(keys[i],menu.getSubMenus());
            }
            return menu;
        }
    }

    private static Menu getSubMenus(String key, List<Menu> menus){
        for(Menu menu : menus){
            if(key.equals(menu.getKey())){
                return menu;
            }
        }
        return null;
    }*/
}
