package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import core.BaseInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * description
 * 
 * @author LSZ 2020/03/25 10:54
 * @contact 648748030@qq.com
 */
@Data
@NoArgsConstructor
@Table(name = "chengyu_log")
public class ChengyuLog extends BaseInfo {

    public static final String ID = "id";
    public static final String CHENGYU = "chengyu";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String chengyu;

    private String lastChengyu;

    private String type;

    private Integer isDeleted;

    public ChengyuLog(String chengyu, String lastChengyu, String type){
        this.chengyu = chengyu;
        this.lastChengyu = lastChengyu;
        this.type = type;
    }
}
