package cn.lsz.gongzhonghao.hajimiemasidie.service.emoji;

import cn.lsz.gongzhonghao.hajimiemasidie.service.ProxySelf;
import core.service.MyBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * description
 * 
 * @author LSZ 2020/05/22 14:51
 * @contact 648748030@qq.com
 */
@Service
@Transactional
public class EmojiService implements ProxySelf<EmojiService> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    public String emojiRule(String userName, String text){
        return "输入任意标签查询表情包，也可在本菜单上传静态表情，上传表情后可输入标签进行保存方便查看\n" +
                "注意事项：\n" +
                "1、标签可以使用特殊符号进行分割，定位更准确\n" +
                "2、微信端暂不支持上传动态表情至服务器\n";
    }

}
