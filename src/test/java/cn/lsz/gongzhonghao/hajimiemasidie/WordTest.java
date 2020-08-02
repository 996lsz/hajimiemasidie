package cn.lsz.gongzhonghao.hajimiemasidie;

import com.alibaba.fastjson.JSONObject;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * description
 * 
 * @author LSZ 2020/03/13 23:28
 * @contact 648748030@qq.com
 */
public class WordTest {

    /*Map pinyinMap = new HashMap(){
        {
            put("ā","a1"); put("á","a2"); put("ǎ","a3"); put("à","a4");
            put("ē","e1"); put("é","e2"); put("ě","e3"); put("è","e4");
            put("ī","i1"); put("í","i2"); put("ǐ","i3"); put("ì","i4");
            put("ō","o1"); put("ó","o1"); put("ǒ","o3"); put("ò","o4");
            put("ū","u1"); put("ú","u2"); put("ǔ","u3"); put("ù","u4");
            put("ü","v1"); put("ǖ","v1"); put("ǘ","v2"); put("ǚ","v3"); put("ǜ","v4");
        }
    };

    @Test
    public void test() throws PinyinException {
        System.out.println(PinyinHelper.convertToPinyinString("度日如年", ",", PinyinFormat.WITH_TONE_NUMBER));
        System.out.println(PinyinHelper.convertToPinyinString("一毛不拔", ",", PinyinFormat.WITH_TONE_NUMBER));
        System.out.println(PinyinHelper.convertToPinyinString("一路平安", ",", PinyinFormat.WITH_TONE_NUMBER));
    }

    @Test
    public void pinyinTest() {
        String pinyin = "bēi shuǐ chē xīn";
        StringBuilder result = new StringBuilder();
        String tempDuYin = "";
        for(int i = 0; i < pinyin.length(); i++){
            String temp = String.valueOf(pinyin.charAt(i));
            if(" ".equals(temp)){
                result.append(tempDuYin + ",");
            }else {
                String trans = (String) pinyinMap.get(temp);
                if (trans != null) {
                    result.append(trans.charAt(0));
                    tempDuYin = String.valueOf(trans.charAt(1));
                } else {
                    result.append(temp);
                }
            }
        }
        result.append(tempDuYin);
        System.out.println(result);
    }

    @Test
    public void getWord() throws IOException, PinyinException {
        StringBuilder success = new StringBuilder("导入成功的成语：\n");
        StringBuilder error = new StringBuilder("导入失败的成语:\n");
        StringBuilder unknow = new StringBuilder("未知成语:\n");
        File file = new File("src\\main\\resources\\idiom.txt");
        List<String> list = FileUtils.readLines(file);
        for(String word : list){
            int strat = word.indexOf("【");
            int end = word.indexOf("】");
            if(strat >= 0 && end > 0){
                word = word.substring(strat + 1, end);
                String pinyin = PinyinHelper.convertToPinyinString(word, ",", PinyinFormat.WITH_TONE_NUMBER);
                if(word.length() == 4 && StringUtils.isNotEmpty(pinyin)){
                    success.append(word + "\n");
                }else{
                    if(word.length() != 4){
                        error.append(word + "(长度不对)\n");
                    }
                    if(StringUtils.isEmpty(pinyin)){
                        error.append(word + "(没有拼音)\n");
                    }
                    if(word.split("，").length == 2){
                        String[] split = word.split("，");
                        if(split[0].length() == 4 || split[1].length() == 4){
                            unknow.append(word + "\n");
                        }
                    }
                }
            }
        }
        System.out.println(error);
        //System.out.println(unknow);
    }

    @Test
    public void apiTest() throws UnsupportedEncodingException {
        String key = "";
        //String testWord = URLEncoder.encode("杯水车薪", "UTF-8");
        String testWord = "杯水车薪";
            String url = String.format("http://v.juhe.cn/chengyu/query?key=%s&word=%s",key,testWord);
            JSONObject wordObject = new RestTemplate().getForObject(url, JSONObject.class);
            System.out.println(wordObject);

    }

    @Test
    public void testEntity(){
        String json = "{\"reason\":\"超过每日可允许请求次数!\",\"error_code\":10012,\"resultcode\":\"112\"}";

        ChengyuQueryResponse response = JSONObject.parseObject(json,ChengyuQueryResponse.class);
        System.out.println(response);
    }
*/
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        String temp = "ff";
        String pinyin = "aaa,bb,cc,dd";

        String n = temp + pinyin.substring(pinyin.indexOf(","));

        System.out.println(n);

    }
}
