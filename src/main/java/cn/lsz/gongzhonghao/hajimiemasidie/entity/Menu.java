package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description
 * 
 * @author LSZ 2020/02/14 17:12
 * @contact 648748030@qq.com
 */
@Data
@NoArgsConstructor
public class Menu {

    private String key;

    private String title;

    private String event;

    private List<Menu> subMenus;

    public Menu(String key, String title){
        this.key = key;
        this.title = title;
    }

    public Menu(String key, String title, String event){
        this.key = key;
        this.title = title;
        this.event = event;
    }
}
