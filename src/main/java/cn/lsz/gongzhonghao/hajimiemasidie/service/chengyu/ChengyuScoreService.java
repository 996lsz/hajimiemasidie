package cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.Chengyu;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuScore;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.WxTextResponse;
import cn.lsz.gongzhonghao.hajimiemasidie.service.ProxySelf;
import cn.lsz.gongzhonghao.hajimiemasidie.service.UserService;
import cn.lsz.gongzhonghao.hajimiemasidie.service.WxMenuService;
import cn.lsz.gongzhonghao.hajimiemasidie.util.XmlBeanUtils;
import com.github.pagehelper.PageHelper;
import core.service.MyBaseService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.ChengyuConstant.*;
import static cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuScore.SCORE;
import static cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuScore.TYPE;
import static core.BaseInfo.DESC;

/**
 * description
 * 
 * @author LSZ 2020/03/28 14:48
 * @contact 648748030@qq.com
 */
@Service
@Transactional
public class ChengyuScoreService extends MyBaseService<ChengyuScore> implements ProxySelf<ChengyuScoreService> {

    @Resource(name = "redisTemplate" )
    private RedisTemplate redisTemplate;

    @Autowired
    private ChengyuService chengyuService;

    @Autowired
    private UserService userService;

    @Autowired
    private WxMenuService wxMenuService;

    /*
    * 创建排行
    * */
    public String createScore(String userName, String name){
        String content = null;
        String scoreCacheKey = String.format(REDIS_CHENGYU_SCORE_KEY, userName);
        if(!("0".equals(name) || "#".equals(name))){
            ChengyuScore score = (ChengyuScore) redisTemplate.opsForValue().get(scoreCacheKey);
            score.setName(name.trim());
            score.setCreateBy(score.getOpenid());
            mapper.insertSelective(score);
            if(score.getRanking() != null && score.getRanking() <= 20){
                self().refreshRankingList(score.getType());
            }
            content = "已创建排名";
        }else{
            content = "已跳过创建排名";
        }
        redisTemplate.delete(scoreCacheKey);
        //redisTemplate.delete(String.format(REDIS_CONTEXT_KEY, userName));
        userService.deleteUserContext(userName);

        return XmlBeanUtils.toXml(new WxTextResponse(content, userName));
    }


    /*
    * 刷新排行榜缓存
    * */
    public void refreshRankingList(String type) {
        String scoreListKey = String.format(REDIS_CHENGYU_SCORE_LIST_KEY, type);
        String refreshFlag = String.format(REDIS_CHENGYU_REFRESH_SCORE_LIST_KEY, type);
        try {
            if(redisTemplate.opsForValue().setIfAbsent(refreshFlag,1)){
                redisTemplate.delete(scoreListKey);
                PageHelper.startPage(1,20);
                PageHelper.orderBy(SCORE + DESC);
                Example example = new Example(ChengyuScore.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo(TYPE, type);
                List<ChengyuScore> chengyuScores = mapper.selectByExample(example);
                redisTemplate.delete(refreshFlag);
                redisTemplate.opsForList().rightPushAll(scoreListKey, chengyuScores);
            }
        }catch (Exception e){
            redisTemplate.delete(refreshFlag);
        }
    }

    /*
    * 查询排行榜
    * */
    public List<ChengyuScore> getRankingList(String type){
        String scoreListKey = String.format(REDIS_CHENGYU_SCORE_LIST_KEY, type);
        List<ChengyuScore> list = redisTemplate.opsForList().range(scoreListKey, 0, -1);
        if(list == null || list.size() == 0){
            refreshRankingList(type);
            return redisTemplate.opsForList().range(scoreListKey, 0, -1);
        }
        return list;
    }

    /*
    * 统计当局战绩
    * */
    public void recordCacheScore(ChengyuScore score) {
        String userName = score.getOpenid();
        String chengyuUsedKey = String.format(REDIS_CHENGYU_USED_KEY, userName);
        List<String> usedChengyu = redisTemplate.opsForList().range(chengyuUsedKey, 0, -1);
        score.setDetails(ArrayUtils.toString(usedChengyu));
        //计算分数
        score.setScore(statisticsScore(score.getType(), score.getVictoryFlag(), usedChengyu));
        //统计排名
        score.setRanking(rankingScore(score.getScore(), score.getType()));
        redisTemplate.opsForValue().set(String.format(REDIS_CHENGYU_SCORE_KEY, userName), score, 1, TimeUnit.MINUTES);

    }

    /*
    * 根据成绩计算当前排名
    * */
    private Integer rankingScore(Integer score, String type) {
        Example example = new Example(ChengyuScore.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andGreaterThan(SCORE, score);
        criteria.andEqualTo(TYPE, type);
        int ranking = mapper.selectCountByExample(example);
        return ranking + 1;
    }

    /*
     * 根据对局详情计算得分
     * */
    private Integer statisticsScore(String type, Boolean victoryFlag, List<String> usedChengyu) {
        Integer score = 0;
        Chengyu last = chengyuService.getChengyuCache(usedChengyu.get(0));
        Chengyu current = null;
        for(int i = 1; i <= usedChengyu.size(); i+=2){
            current = chengyuService.getChengyuCache(usedChengyu.get(i));
            if(current != null) {
                if (chengyuService.isSameInitialWord(last, current)) {
                    score += 150;
                } else if (chengyuService.isSameInitialDuyin(last, current)) {
                    score += 50;
                } else if (chengyuService.isLikeInitialDuyin(last, current)) {
                    score += 20;
                }
                if(i+1 < usedChengyu.size()) {
                    last = chengyuService.getChengyuCache(usedChengyu.get(i + 1));
                }
            }
        }

        if(victoryFlag){
            score += 1000;
        }

        return score;
    }

    public String chengyuNormalRankingList(String userName, String text){
        return chengyuRankingList(userName, ChengyuTypeEnum.NORMAL.getType());
    }

    public String chengyuSamePronunciationRankingList(String userName, String text){
        return chengyuRankingList(userName, ChengyuTypeEnum.SAME.getType());
    }

    public String chengyuLikePronunciationRankingList(String userName, String text){
        return chengyuRankingList(userName, ChengyuTypeEnum.LIKE.getType());
    }

    public String chengyuRankingList(String userName, String type){
        //排名占2个字符，称号占8个字符，分数占4个字符，难度占2个字符（默认间隔2个字符）
        StringBuilder result = new StringBuilder("输入排名编号可查看对局详情（输入0或#返回）\n排名，分数，难度，胜负，称号\n");
        String userCurrentRankingListKey = String.format(REDIS_CHENGYU_USER_SCORE_LIST_KEY, userName);
        redisTemplate.delete(userCurrentRankingListKey);
        List<ChengyuScore> rankingList = getRankingList(type);
        redisTemplate.opsForList().rightPushAll(userCurrentRankingListKey, rankingList);
        //数据对齐
        for (int i = 0; i < rankingList.size(); i++) {
            ChengyuScore gameScore = rankingList.get(i);
            String ranking = String.valueOf(i + 1);
            String score = gameScore.getScore().toString();
            String name = gameScore.getName();
            String vitory = gameScore.getVictoryFlag() == true ? "胜" : "负";
            if(ranking.length() < 2){
                ranking += " ";
            }
            if(score.length() < 4){
                int lack = 4 - score.length();
                while (lack > 0) {
                    score = "0" + score;
                    lack --;
                }
            }
            if(name.length() > 7){
                name = name.substring(0,6)+"...";
            }
            String level = CHENGYU_LEVEL_DESC_MAP.get(gameScore.getLevel());

            result.append(ranking).append("     ").append(score).append("     ").append(level).append("     ").append(vitory).append("     ").append(name).append("\n");
        }
        //变更当前上下文为查看对局详情
        userService.setUserContext(userName, "chengyuScoreService.chengyuRankingDetails", 10, TimeUnit.MINUTES);
        return XmlBeanUtils.toXml(new WxTextResponse(result.toString(), userName));
    }

    /*
    * 查询对局详情
    * */
    public String chengyuRankingDetails(String userName, String text){
        if("0".equals(text) || "#".equals(text)){
            userService.deleteUserContext(userName);
            return wxMenuService.getUserMenu(userName);
        }
        if(StringUtils.isNumeric(text)){
            Integer index = Integer.parseInt(text) - 1 ;
            ChengyuScore score = (ChengyuScore) redisTemplate.opsForList().index(String.format(REDIS_CHENGYU_USER_SCORE_LIST_KEY, userName), index);
            if(score != null){
                return XmlBeanUtils.toXml(new WxTextResponse(score.getName() + " 的对局：\n" + score.getDetails(), userName));
            }else{
                return XmlBeanUtils.toXml(new WxTextResponse("不存在的记录", userName));
            }
        }
        userService.deleteUserContext(userName);
        return wxMenuService.getUserMenu(userName);
    }

}
