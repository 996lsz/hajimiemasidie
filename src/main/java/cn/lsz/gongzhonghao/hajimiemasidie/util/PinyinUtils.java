package cn.lsz.gongzhonghao.hajimiemasidie.util;

import com.github.stuxuhai.jpinyin.PinyinException;

import java.util.HashMap;
import java.util.Map;

/**
 * description
 * 
 * @author LSZ 2020/03/16 17:21
 * @contact 648748030@qq.com
 */
public class PinyinUtils {

    private static final Map PINYIN_MAP = new HashMap(){
        {
            put("ā","a1"); put("á","a2"); put("ǎ","a3"); put("à","a4");
            put("ē","e1"); put("é","e2"); put("ě","e3"); put("è","e4");
            put("ī","i1"); put("í","i2"); put("ǐ","i3"); put("ì","i4");
            put("ō","o1"); put("ó","o1"); put("ǒ","o3"); put("ò","o4");
            put("ū","u1"); put("ú","u2"); put("ǔ","u3"); put("ù","u4");
            put("ü","v1"); put("ǖ","v1"); put("ǘ","v2"); put("ǚ","v3"); put("ǜ","v4");
        }
    };

    public static String toStr(String pinyin) {
        StringBuilder result = new StringBuilder();
        String tempDuYin = "";
        for(int i = 0; i < pinyin.length(); i++){
            String temp = String.valueOf(pinyin.charAt(i));
            if(" ".equals(temp)){
                result.append(tempDuYin + ",");
            }else {
                String trans = (String) PINYIN_MAP.get(temp);
                if (trans != null) {
                    result.append(trans.charAt(0));
                    tempDuYin = String.valueOf(trans.charAt(1));
                } else {
                    result.append(temp);
                }
            }
        }
        result.append(tempDuYin);
        return result.toString();
    }
}
