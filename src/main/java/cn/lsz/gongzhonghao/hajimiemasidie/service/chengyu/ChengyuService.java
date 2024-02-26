package cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.Chengyu;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuAutoAdd;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuScore;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.WxTextResponse;
import cn.lsz.gongzhonghao.hajimiemasidie.service.ProxySelf;
import cn.lsz.gongzhonghao.hajimiemasidie.service.UserService;
import cn.lsz.gongzhonghao.hajimiemasidie.util.XmlBeanUtils;
import core.service.MyBaseService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.ChengyuConstant.*;
import static cn.lsz.gongzhonghao.hajimiemasidie.constant.ChengyuConstant.ChengyuTypeEnum.*;

/**
 * description
 * 
 * @author LSZ 2020/03/15 10:14
 * @contact 648748030@qq.com
 */
@Service
@Transactional
public class ChengyuService extends MyBaseService<Chengyu> implements ProxySelf<ChengyuService> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "redisTemplate" )
    private RedisTemplate redisTemplate;

    @Autowired
    private ChengyuLogService chengyuLogService;

    @Autowired
    private ChengyuAutoAddService chengyuAutoAddService;

    @Autowired
    private ChengyuScoreService chengyuScoreService;

    @Autowired
    private UserService userService;

    public String chengyuNormalEasy(String userName, String chengyu){
        //更新公众号上下文(与event事件一致)
        userService.setUserContext(userName, "chengyuService.chengyuNormalEasy", 60, TimeUnit.SECONDS);
        ChengyuScore score =  chengyuGame(userName, chengyu, NORMAL, ChengyuLevelEnum.EASY);
        return scoreContent(score);
    }

    public String chengyuSamePronunciationEasy(String userName, String chengyu){
        //更新公众号上下文(与event事件一致)
        userService.setUserContext(userName, "chengyuService.chengyuSamePronunciationEasy", 60, TimeUnit.SECONDS);
        ChengyuScore score = chengyuGame(userName, chengyu, SAME, ChengyuLevelEnum.EASY);
        return scoreContent(score);
    }

    public String chengyuLikePronunciationEasy(String userName, String chengyu){
        //更新公众号上下文(与event事件一致)
        userService.setUserContext(userName, "chengyuService.chengyuLikePronunciationEasy", 60, TimeUnit.SECONDS);
        ChengyuScore score = chengyuGame(userName, chengyu, LIKE, ChengyuLevelEnum.EASY);
        return scoreContent(score);
    }

    public String chengyuNormalHard(String userName, String chengyu){
        //更新公众号上下文(与event事件一致)
        userService.setUserContext(userName, "chengyuService.chengyuNormalHard", 60, TimeUnit.SECONDS);
        ChengyuScore score = chengyuGame(userName, chengyu, NORMAL, ChengyuLevelEnum.HARD);
        return scoreContent(score);
    }

    public String chengyuSamePronunciationHard(String userName, String chengyu){
        //更新公众号上下文(与event事件一致)
        userService.setUserContext(userName, "chengyuService.chengyuSamePronunciationHard", 60, TimeUnit.SECONDS);
        ChengyuScore score = chengyuGame(userName, chengyu, SAME, ChengyuLevelEnum.HARD);
        return scoreContent(score);
    }

    public String chengyuLikePronunciationHard(String userName, String chengyu){
        //更新公众号上下文(与event事件一致)
        userService.setUserContext(userName, "chengyuService.chengyuLikePronunciationHard", 60, TimeUnit.SECONDS);
        ChengyuScore score = chengyuGame(userName, chengyu, LIKE, ChengyuLevelEnum.HARD);
        return scoreContent(score);
    }

    private String scoreContent(ChengyuScore score){
        Boolean victoryFlag =  score.getVictoryFlag();
        String openid = score.getOpenid();
        WxTextResponse response = new WxTextResponse(openid);
        String content = null;
        if(victoryFlag == null){
            //未分胜负
            content = score.getContent();
        }else if(victoryFlag == true){
            //胜出
            content = String.format("在与我交战的对手中，没人比你更强，在下愿称您为最强！\n您的分数为：%s\n排名%s\n请输入您的称号（输入后将记录在排行榜上，输入#或0跳过）：",score.getScore() ,score.getRanking());
        }else{
            //失败
            content = String.format("你也想起舞吗？\n您的分数为：%s\n排名%s\n请输入您的称号（输入后将记录在排行榜上，输入#或0跳过）：",score.getScore() ,score.getRanking());
        }
        response.setContent(content);
        return XmlBeanUtils.toXml(response);
    }

    //@MethodSynchronizedAnnotation(redisKey = REDIS_USING_FLAG_KEY, keys = {"#p1","chengyu_game"}, timeOut = 5, timeUnit = TimeUnit.SECONDS)
    private ChengyuScore chengyuGame(String userName, String chengyu, ChengyuTypeEnum type, ChengyuLevelEnum level){
        ChengyuScore score = new ChengyuScore(type.getType(), userName, level.getLevel());
        String nextChengyu;
        String chengyuUsedKey = String.format(REDIS_CHENGYU_USED_KEY, userName);

        if(redisTemplate.opsForList().size(chengyuUsedKey) == 0){
            nextChengyu = randomChengyu(level.getLevel());
        }else {
            String pinyin = null;
            String voice = null;
            String initial = null;
            //校验成语正确性
            String validate = validateChengyu(userName, chengyu, type);
            //记录用户使用成语
            redisTemplate.opsForList().rightPush(chengyuUsedKey, chengyu);
            if (validate != null) {
                //校验不通过
                score.setVictoryFlag(false);
                recordScore(score);
                score.setContent(validate);
                return score;
            }
            List<Object> usedList = redisTemplate.opsForList().range(chengyuUsedKey, 0, -1);
            //获取成语最后一个字，随机取一个成语
            Chengyu currentChengyu = getChengyuCache(chengyu);
            //redisTemplate.opsForValue().get(String.format(REDIS_CHENGYU_KEY, chengyu));
            if (type == NORMAL) {
                initial = String.valueOf(currentChengyu.getChengyu().charAt(3));
            }
            if (type == SAME) {
                String duyin = currentChengyu.getSpell().split(",")[3];
                pinyin = duyin.substring(0, duyin.length() - 1);
                voice = String.valueOf(duyin.charAt(duyin.length() - 1));
            }
            if (type == LIKE) {
                String duyin = currentChengyu.getSpell().split(",")[3];
                pinyin = duyin.substring(0, duyin.length() - 1);
            }
            List<String> allList = getInitialChengyuCache(pinyin, voice, initial, level.getLevel());
            if (allList == null) {
                score.setVictoryFlag(true);
                recordScore(score);
                return score;
            }
            //allList排除掉已使用的成语，为未使用的成语，从中随机
            if (usedList != null) {
                allList.removeAll(usedList);
            }
            //没有可用成语
            if (allList.size() == 0) {
                score.setVictoryFlag(true);
                recordScore(score);
                return score;
            }

            Random random = new Random();
            int i = random.nextInt(allList.size());
            nextChengyu = allList.get(i);
        }

        //更新已使用成语缓存
        redisTemplate.opsForList().rightPush(chengyuUsedKey, nextChengyu);
        score.setContent(nextChengyu);
        return score;
    }


    public String validateChengyu(String userName, String chengyu, ChengyuTypeEnum type){
        String chengyuUsedKey = String.format(REDIS_CHENGYU_USED_KEY, userName);
        //上一个成语接龙成语
        Long index = redisTemplate.opsForList().size(chengyuUsedKey);
        String lastChengyuStr = (String) redisTemplate.opsForList().index(chengyuUsedKey, index - 1);
        Chengyu lastChengyu = getChengyuCache(lastChengyuStr);
        //成语长度校验
        if(chengyu.length() != 4){
            return "成语错误,GAME OVER";
        }
        //当前成语有效性校验
        Chengyu currentChengyu = getChengyuCache(chengyu);
        if(currentChengyu == null){
            //保存未被识别的成语
            self().logChengyu(chengyu, lastChengyuStr ,type, userName);
            return "成语错误,GAME OVER";
        }
        //成语接龙首尾比较
        if(lastChengyu != null) {
            switch (type) {
                //常规玩法，尾字与首字一致，例：万里挑一  ->  一意孤行（一 : 一）
                case NORMAL: {
                    if (!isSameInitialWord(lastChengyu, currentChengyu)) {
                        return "牛头不对马嘴,GAME OVER";
                    }
                }
                //同音字玩法，尾字读音与首字一致，例：万里挑一  ->  依依不舍（yi1 : yi1）,或尾字与首字一致，
                case SAME: {
                    if (!(isSameInitialDuyin(lastChengyu, currentChengyu) || isSameInitialWord(lastChengyu, currentChengyu))) {
                        return "牛头不对马嘴,GAME OVER";
                    }
                }
                //近音字玩法，尾字单词与首字一致，例：万里挑一  ->  异口同声（yi : yi）,或尾字与首字一致，
                case LIKE: {
                    if (!(isLikeInitialDuyin(lastChengyu, currentChengyu) || isSameInitialWord(lastChengyu, currentChengyu))) {
                        return "牛头不对马嘴,GAME OVER";
                    }
                }
                default: {
                    break;
                }
            }
        }
        //校验成语是否重复使用
        List<Object> usedList = redisTemplate.opsForList().range(chengyuUsedKey, 0, -1);
        if(usedList != null && usedList.contains(chengyu)){
            return "成语已经用过了喔,GAME OVER";
        }
        return null;
    }

    //判断两个成语是否首尾字相同
    public Boolean isSameInitialWord(Chengyu lastChengyu, Chengyu currentChengyu){
        String lastInitial = String.valueOf(lastChengyu.getChengyu().charAt(3));
        String currentInitial = String.valueOf(currentChengyu.getChengyu().charAt(0));
        if (lastInitial.equals(currentInitial)) {
            return true;
        }
        return false;
    }

    //判断两个成语是否首尾同音字
    public Boolean isSameInitialDuyin(Chengyu lastChengyu, Chengyu currentChengyu){
        String lastInitial = lastChengyu.getSpell().split(",")[3];
        String currentInitial = String.valueOf(currentChengyu.getSpell().split(",")[0]);
        if (lastInitial.equals(currentInitial)) {
            return true;
        }
        return false;
    }

    //判断两个成语是否首尾形音字
    public Boolean isLikeInitialDuyin(Chengyu lastChengyu, Chengyu currentChengyu){
        String lastInitial = lastChengyu.getSpell().split(",")[3];
        lastInitial = lastInitial.substring(0, lastInitial.length() - 1);
        String currentInitial = String.valueOf(currentChengyu.getSpell().split(",")[0]);
        currentInitial = currentInitial.substring(0, currentInitial.length() - 1);
        if (lastInitial.equals(currentInitial)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param pinyin 成语的首字拼音
     * @param voice 成语的首字读音
     * @param initial 成语的首字
     * @param level 难度（目前只分了两级，简单（1）以及困难（null））
     * @return 符合条件的所有成语List<String>
     */
    public List<String> getInitialChengyuCache(String pinyin, String voice, String initial, String level){
        List<String> list = new ArrayList<>();
        pinyin = StringUtils.defaultString(pinyin,"*");
        voice = StringUtils.defaultString(voice,"*");
        initial = StringUtils.defaultString(initial,"*");
        level = StringUtils.defaultString(level,"*");
        Set<String> keys = redisTemplate.keys(String.format(REDIS_CHENGYU_INITIAL_KEY, pinyin, voice, initial, level));
        for(String key : keys){
            List values = redisTemplate.opsForList().range(key, 0, -1);
            if(values != null && values.size() != 0){
                list.addAll(values);
            }
        }
        return list;
    }

    /**
     * @desc 随机返回一个成语，用于用户第一次请求成语接龙
     * @return 符合条件的所有成语List<String>
     */
    public String randomChengyu(String level){
        int temp = 0;
        String randomKey = null;
        Random random = new Random();
        level = StringUtils.defaultString(level,"*");
        Set<String> keys = redisTemplate.keys(String.format(REDIS_CHENGYU_INITIAL_KEY, "*", "*", "*", level));
        int index = random.nextInt(keys.size());
        for(String key : keys){
            if(temp != index){
                temp ++;
            }else{
                randomKey = key;
                break;
            }
        }
        List<Object> chengyuList = redisTemplate.opsForList().range(randomKey, 0, -1);
        index = random.nextInt(chengyuList.size());
        return (String) chengyuList.get(index);
    }


    public Chengyu getChengyuCache(String chengyu) {
        String key = String.format(REDIS_CHENGYU_KEY, chengyu);
        Chengyu chengyuEntity = (Chengyu) redisTemplate.opsForValue().get(key);
        if(chengyuEntity != null){
            return chengyuEntity;
        }else{
            chengyuEntity = new Chengyu(chengyu);
            chengyuEntity = mapper.selectOne(chengyuEntity);
            if(chengyuEntity != null){
                redisTemplate.opsForValue().set(key, chengyuEntity);
            }
            return chengyuEntity;
        }
    }


    public Chengyu saveOrUpdate(Chengyu chengyu) {
        Chengyu cache = getChengyuCache(chengyu.getChengyu());
        if(cache != null){
            chengyu.setId(cache.getId());
            chengyu.setLastUpdateDate(new Date());
            mapper.updateByPrimaryKeySelective(chengyu);
            removeChengyuInitialCache(cache);
        }else{
            try {
                mapper.insertSelective(chengyu);
            } catch (DuplicateKeyException e){}
        }
        Chengyu dbChengyu = new Chengyu(chengyu.getChengyu());
        dbChengyu = mapper.selectOne(dbChengyu);
        initChengyuInitialCache(dbChengyu);
        redisTemplate.opsForValue().set(String.format(REDIS_CHENGYU_KEY, dbChengyu.getChengyu()), dbChengyu);
        return dbChengyu;
    }

    public void init(){
        //这里可能会有数据过大内存放不下的问题，如果出现需要做分页
        List<Chengyu> chengyuList = mapper.selectAll();
/*        self().initChengyuCache(chengyuList);
        self().initChengyuInitialCache(chengyuList);*/
        initChengyuCache(chengyuList);
        initChengyuInitialCache(chengyuList);
    };

    private void recordScore(ChengyuScore score){
        String userName = score.getOpenid();
        //删除已使用成语缓存
        String chengyuUsedKey = String.format(REDIS_CHENGYU_USED_KEY, userName);

        //记录当前成绩以及更改上下文为签名
        chengyuScoreService.recordCacheScore(score);
        //设置下文为输入用户名以创建排名
        userService.setUserContext(userName, "chengyuScoreService.createScore", 1, TimeUnit.MINUTES);

        redisTemplate.delete(chengyuUsedKey);
    }

    public void remove(String chengyuStr) {
        Chengyu chengyu = new Chengyu(chengyuStr);
        chengyu = mapper.selectOne(chengyu);
        remove(chengyu);
    }

    public void remove(Chengyu chengyu) {
        //删除缓存
        redisTemplate.delete(String.format(REDIS_CHENGYU_KEY, chengyu.getChengyu()));
        removeChengyuInitialCache(chengyu);
        mapper.deleteByPrimaryKey(chengyu);
    }

    private void removeChengyuInitialCache(Chengyu chengyu) {
        String duyin = chengyu.getSpell().split(",")[0];
        String pinyin = duyin.substring(0, duyin.length() - 1);
        String voice = String.valueOf(duyin.charAt(duyin.length() - 1));
        String level = String.valueOf(chengyu.getLevel());
        String cacheKey = String.format(REDIS_CHENGYU_INITIAL_KEY, pinyin, voice, chengyu.getChengyu().substring(0,1), level);
        redisTemplate.opsForList().remove(cacheKey, -1, chengyu.getChengyu());
    }

    @Async
    public void initChengyuCache(List<Chengyu> chengyuList){
        Map<String, Chengyu> map = new HashMap<>();
        for (Chengyu chengyu : chengyuList) {
            String cacheKey = String.format(REDIS_CHENGYU_KEY, chengyu.getChengyu());
            map.put(cacheKey, chengyu);
        }
        redisTemplate.opsForValue().multiSet(map);
    }

    public void initChengyuCache(Chengyu chengyu){
        String cacheKey = String.format(REDIS_CHENGYU_KEY, chengyu.getChengyu());
        redisTemplate.opsForValue().set(cacheKey, chengyu);
    }

    @Async
    public void initChengyuInitialCache(List<Chengyu> chengyuList){
        Set<String> keys = redisTemplate.keys(String.format(REDIS_CHENGYU_INITIAL_KEY, "*", "*", "*", "*"));
        redisTemplate.delete(keys);
        chengyuList.parallelStream().forEach(chengyu -> initChengyuInitialCache(chengyu));
    }

    public void initChengyuInitialCache(Chengyu chengyu){
        String duyin = chengyu.getSpell().split(",")[0];
        String pinyin = duyin.substring(0, duyin.length() - 1);
        String voice = String.valueOf(duyin.charAt(duyin.length() - 1));
        String level = String.valueOf(chengyu.getLevel());
        String cacheKey = String.format(REDIS_CHENGYU_INITIAL_KEY, pinyin, voice, chengyu.getChengyu().substring(0,1), level);
        redisTemplate.opsForList().rightPush(cacheKey,chengyu.getChengyu());
    }

    @Async
    public void logChengyu(String chengyu, String lastChengyu, ChengyuTypeEnum type, String userName){
        //前期版本使用的记录未被识别的成语
        //MyLogUtils.info(chengyu);
        if(chengyuAutoAddService.select(new ChengyuAutoAdd(chengyu)).size() == 0) {
            chengyuLogService.insertSelective(chengyu, lastChengyu, type.getType(), userName);
        }
    }

    public String chengyuNormaRule(String userName, String text){
        String rule = "成语接龙规则（常规）:\n输入的成语首字必须与上一成语的尾字相同\n如：难上加难 -> \n难舍难分 -> \n分秒必争";
        return XmlBeanUtils.toXml(new WxTextResponse(rule, userName));
    }

    public String chengyuSamePronunciationRule(String userName, String text){
        String rule = "成语接龙规则（同音字）:\n输入的成语首字必须与上一成语的尾字为同音字（亦或者符合常规玩法规则）\n如：难上加难（nán） -> \n（nán）南柯一梦（mèng） -> \n（mèng）孟母三迁";
        return XmlBeanUtils.toXml(new WxTextResponse(rule, userName));
    }

    public String chengyuLikePronunciationRule(String userName, String text){
        String rule = "成语接龙规则（形音字）:\n输入的成语首字必须与上一成语的尾字为形音字（亦或者符合常规玩法规则）\n如：难上加难(nan) -> \n(nan)难兄难弟(di) -> \n(di)滴水不漏";
        return XmlBeanUtils.toXml(new WxTextResponse(rule, userName));
    }
}
