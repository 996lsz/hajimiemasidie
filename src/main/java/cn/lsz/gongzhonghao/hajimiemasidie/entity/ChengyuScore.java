package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import core.BaseInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * description
 * 
 * @author LSZ 2020/03/28 14:42
 * @contact 648748030@qq.com
 */
@Data
@NoArgsConstructor
@Table(name = "chengyu_score")
public class ChengyuScore extends BaseInfo {

    public static final String SCORE = "score";
    public static final String TYPE = "type";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String openid;

    private String name;

    private Integer score;

    private String details;

    private String type;

    private String level;

    private Boolean victoryFlag;

    @Transient
    private Integer ranking;

    @Transient
    private String content;

    public ChengyuScore(String openid){
        this.openid = openid;
    }

    public ChengyuScore(String type, String openid){
        this.type = type;
        this.openid = openid;
    }

    public ChengyuScore(String type, String openid, String level){
        this.type = type;
        this.openid = openid;
        this.level = level;
    }

    public ChengyuScore(String type, Boolean victoryFlag){
        this.type = type;
        this.victoryFlag = victoryFlag;
    }

}
