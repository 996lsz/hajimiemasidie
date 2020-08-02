package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信基础事件属性
 * 
 * @author LSZ 2020/02/13 10:26
 * @contact 648748030@qq.com
 */
@XStreamAlias("xml")
@Data
public class WxBaseEvent extends WxBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("Event")
    protected String event;

}
