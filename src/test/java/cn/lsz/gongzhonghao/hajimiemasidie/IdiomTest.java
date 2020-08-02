package cn.lsz.gongzhonghao.hajimiemasidie;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * description
 * 
 * @author LSZ 2020/03/14 16:00
 * @contact 648748030@qq.com
 */
public class IdiomTest {

    /*private static Set idiomSet = new HashSet<String>();
    private static Map idiomInitialMap = new MultiValueMap();
    private static Map idiomUsedMap = new MultiValueMap();

    private static String initial = "";

    public static void main(String[] args) throws Exception {
        //初始化单词接龙数据
        //createIdiomMap();
        createIdiomMap2();
        Scanner scanner = new Scanner(System.in);
        System.out.println("成语接龙:");
        while (true){
            String word = scanner.nextLine();
            if("break".equals(word)){
                break;
            }
            //必须为已存在且未使用过的成语
            if(!idiomSet.contains(word) || word.length() != 4){
                System.out.println("成语错误");
                break;
            }
            if(initial.equals(String.valueOf(word.charAt(0))) && "" != initial){
                System.out.println("牛头不对马嘴");
                break;
            }
            if(idiomUsedMap.get(initial) != null ){
                List<String> userWords = (List<String>) idiomUsedMap.get(initial);
                if(userWords.contains(word)){
                    System.out.println("成语已经用过了喔");
                    break;
                }
            }
            //获取成语最后一个字，随机取一个成语
            initial = String.valueOf(word.charAt(3));
            List<String> allWords = (List<String>) idiomInitialMap.get(initial);
            List<String> usedWords = (List<String>) idiomUsedMap.get(initial);
            if(allWords == null){
                System.out.println("在下愿称你为最强！");
                break;
            }
            List<String> unUsedWords = new ArrayList<>();
            unUsedWords.addAll(allWords);
            if(usedWords != null){
                unUsedWords.removeAll(usedWords);
            }
            Random random = new Random();
            int i = random.nextInt(unUsedWords.size());
            String newWord = unUsedWords.get(i);
            //保存已使用的成语,记录首字
            idiomUsedMap.put(String.valueOf(newWord.charAt(0)),word);
            System.out.println(newWord);
        }
    }

    private static void createIdiomMap() throws IOException, PinyinException {
        File file = new File("src\\main\\resources\\idiom.txt");
        List<String> list = FileUtils.readLines(file);
        for(String word : list){
            int strat = word.indexOf("【");
            int end = word.indexOf("】");
            if(strat >= 0 && end > 0){
                word = word.substring(strat + 1, end);
                String pinyin = PinyinHelper.convertToPinyinString(word, ",", PinyinFormat.WITH_TONE_NUMBER);

                if(word.length() == 4 && StringUtils.isNotEmpty(pinyin)){

                    idiomSet.add(word);
                    idiomInitialMap.put(String.valueOf(word.charAt(0)),word);
                }
            }
        }
    }

    private static void createIdiomMap2() throws IOException, PinyinException {
        File file = new File("src\\main\\resources\\idiom.json");
        String json = FileUtils.readFileToString(file);
        JSONArray array = JSONArray.parseArray(json);
        for(Object temp : array){
            JSONObject wordObject = (JSONObject) temp;
            String word = wordObject.getString("word");
            idiomSet.add(word);
            idiomInitialMap.put(String.valueOf(word.charAt(0)),word);
        }

    }*/
}
