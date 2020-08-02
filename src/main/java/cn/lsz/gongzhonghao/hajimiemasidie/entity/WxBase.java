package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import cn.lsz.gongzhonghao.hajimiemasidie.util.XStreamCDataConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description
 * 
 * @author LSZ 2020/02/12 17:23
 * @contact 648748030@qq.com
 */
@XStreamAlias("xml")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("ToUserName")
    @XStreamConverter(XStreamCDataConverter.class)
    protected String toUserName;

    @XStreamAlias("FromUserName")
    @XStreamConverter(XStreamCDataConverter.class)
    protected String fromUserName;

    @XStreamAlias("CreateTime")
    protected Long createTime;

    @XStreamAlias("MsgType")
    @XStreamConverter(XStreamCDataConverter.class)
    protected String msgType;

}
