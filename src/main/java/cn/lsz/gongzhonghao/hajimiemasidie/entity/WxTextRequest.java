package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信用户对话内容
 * 
 * @author LSZ 2020/02/13 10:26
 * @contact 648748030@qq.com
 */
@XStreamAlias("xml")
@Data
public class WxTextRequest extends WxBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("Content")
    protected String content;

}
