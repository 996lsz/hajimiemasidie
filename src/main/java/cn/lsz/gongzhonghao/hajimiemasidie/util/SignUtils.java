package cn.lsz.gongzhonghao.hajimiemasidie.util;

import cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;


/**
 * description
 * 
 * @author LSZ 2020/02/13 16:47
 * @contact 648748030@qq.com
 */
public class SignUtils {

    public static String sha1(String timestamp, String nonce) {
        String[] paramArray = {AppConstant.getToken(), timestamp, nonce};
        Arrays.sort(paramArray);
        String patamStr = paramArray[0] + paramArray[1] + paramArray[2];
        return DigestUtils.sha1Hex(patamStr);
    }
}
