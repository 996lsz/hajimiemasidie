package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import core.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * description
 * 
 * @author LSZ 2020/02/13 13:44
 * @contact 648748030@qq.com
 */
@Data
@NoArgsConstructor
@Table(name = "chengyu")
public class Chengyu extends BaseInfo {

    public static final String ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String chengyu;

    private String spell;

    private Integer level;

    public Chengyu(String chengyu){
        this.chengyu = chengyu;
    }

    public Chengyu(String chengyu, String spell){
        this.chengyu = chengyu;
        this.spell = spell;
        this.level = 2;
    }

    public Chengyu(String chengyu, String spell, Integer level){
        this.chengyu = chengyu;
        this.spell = spell;
        this.level = level;
    }
}
