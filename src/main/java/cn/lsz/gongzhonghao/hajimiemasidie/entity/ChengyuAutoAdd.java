package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import core.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * description
 * 
 * @author LSZ 2020/02/13 13:44
 * @contact 648748030@qq.com
 */
@Data
@NoArgsConstructor
@Table(name = "chengyu_auto_add")
public class ChengyuAutoAdd extends Chengyu {

    private Integer updateFlag;

    public ChengyuAutoAdd(UpdateFlagEnum updateFlag){
        this.updateFlag = updateFlag.getType();
    }

    public ChengyuAutoAdd(Chengyu chengyu){
        super(chengyu.getChengyu(), chengyu.getSpell(), chengyu.getLevel());
    }

    public ChengyuAutoAdd(String chengyu){
        super(chengyu);
    }

    @AllArgsConstructor
    @Getter
    public enum UpdateFlagEnum{
        /*新增*/
        NEW(0),
        /*待修改*/
        COMFIRM(1),
        /*已修改*/
        SUCCESS(2),
        /*待删除*/
        TO_BE_DELETED(3),
        /*已删除*/
        DELETED(4);

        private Integer type;

    }

}
