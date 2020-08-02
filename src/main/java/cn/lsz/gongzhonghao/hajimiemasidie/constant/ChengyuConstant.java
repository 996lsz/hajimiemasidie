package cn.lsz.gongzhonghao.hajimiemasidie.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * description
 * 
 * @author LSZ 2020/03/17 17:20
 * @contact 648748030@qq.com
 */
public class ChengyuConstant {

    static {
        Map<String, String> map = new HashMap<>();
        for (ChengyuLevelEnum level : ChengyuLevelEnum.values()) {
            map.put(level.getLevel(), level.getDesc());
        }
        CHENGYU_LEVEL_DESC_MAP = map;
    }

    //用于保存所有的成语信息
    public static final String REDIS_CHENGYU_KEY = "hajimiemasidie:chengyu:cache:%s";
    //用于保存首读音成语List，三个占位符分别为：拼音：音调：首字：难度
    public static final String REDIS_CHENGYU_INITIAL_KEY = "hajimiemasidie:chengyu:initial:%s:%s:%s:%s";
    //用于记录已使用过的成语
    public static final String REDIS_CHENGYU_USED_KEY = "hajimiemasidie:user:chengyu:used:%s";
    //用于项目启动时是否刷新成语接龙缓存
    public static final String REDIS_CHENGYU_REFRESH_KEY = "hajimiemasidie:chengyu:refresh";
    //用于记录成语接龙成绩
    public static final String REDIS_CHENGYU_SCORE_KEY = "hajimiemasidie:user:chengyu:score:%s";
    //用于刷新成语接龙排行榜标识
    public static final String REDIS_CHENGYU_REFRESH_SCORE_LIST_KEY = "hajimiemasidie:chengyu:refresh-score-list-flag:%s";
    //成语接龙排行榜
    public static final String REDIS_CHENGYU_SCORE_LIST_KEY = "hajimiemasidie:chengyu:score-list:%s";
    //用于保存用户当前查询的排行榜，防止幻读
    public static final String REDIS_CHENGYU_USER_SCORE_LIST_KEY = "hajimiemasidie:user:score-list:%s";


    //用于保存难度说明
    public static final Map<String, String> CHENGYU_LEVEL_DESC_MAP;

    @AllArgsConstructor
    @Getter
    public enum ChengyuTypeEnum{
        /*常规*/
        NORMAL("1"),
        /*同音字*/
        SAME("2"),
        /*近音字*/
        LIKE("3");

        private String type;

    }

    @AllArgsConstructor
    @Getter
    public enum ChengyuLevelEnum{
        /*简单*/
        EASY("1","简单"),
        /*困难*/
        HARD("*","困难");

        private String level;

        private String desc;

    }

}
