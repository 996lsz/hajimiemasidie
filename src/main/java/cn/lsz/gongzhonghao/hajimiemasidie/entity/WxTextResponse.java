package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant;
import cn.lsz.gongzhonghao.hajimiemasidie.util.XStreamCDataConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description
 * 
 * @author LSZ 2020/02/13 15:12
 * @contact 648748030@qq.com
 */
@XStreamAlias("xml")
@Data
@NoArgsConstructor
public final class WxTextResponse extends WxBase implements Serializable {

    @XStreamAlias("Content")
    @XStreamConverter(XStreamCDataConverter.class)
    private String content;

    public WxTextResponse(String content, String toUserName){
        this.content = content;
        this.toUserName = toUserName;
        this.fromUserName = AppConstant.getWechatId();
        this.createTime = System.currentTimeMillis() / 1000;
        this.msgType = "text";
    }

    public WxTextResponse(String toUserName){
        this.toUserName = toUserName;
        this.fromUserName = AppConstant.getWechatId();
        this.createTime = System.currentTimeMillis() / 1000;
        this.msgType = "text";
    }

}
