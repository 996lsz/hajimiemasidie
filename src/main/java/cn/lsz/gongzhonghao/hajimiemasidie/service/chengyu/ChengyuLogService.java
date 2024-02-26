package cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu;

import cn.lsz.gongzhonghao.hajimiemasidie.constant.ChengyuConstant;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.Chengyu;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuLog;
import cn.lsz.gongzhonghao.hajimiemasidie.mapper.ChengyuLogMapper;
import cn.lsz.gongzhonghao.hajimiemasidie.service.ProxySelf;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import core.service.MyBaseService;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuLog.CHENGYU;

/**
 * 该类是用于持续收录新成语的方法，但是觉得正确性得不到保障然后停用了
 * 
 * @author LSZ 2020/03/25 11:01
 * @contact 648748030@qq.com
 */
@Service
@Transactional
public class ChengyuLogService extends MyBaseService<ChengyuLog> implements ProxySelf<ChengyuLogService> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChengyuLogMapper logMapper;

    @Autowired
    private ChengyuService chengyuService;

    public void insertSelective(String chengyu, String lastChengyu, String type, String userName) {
        ChengyuLog log = new ChengyuLog(chengyu, lastChengyu, type);
        log.setCreateBy(userName);
        mapper.insertSelective(log);
    }

    public MultiValueMap selectMentionChengyu(){
        MultiValueMap result = new MultiValueMap();
        List<ChengyuLog> chengyuLogList = logMapper.selectMentionChengyu();
        for(ChengyuLog log : chengyuLogList){
            result.put(log.getChengyu(), log);
        }
        return result;
    }

    /**
     * 通过数据占比决定成语首字使用什么读音，并返回Chengyu 实体类
     * @param records
     * @return Chengyu
     */
    public Chengyu decideInitial(List<ChengyuLog> records) throws PinyinException {
        Chengyu result = null;
        MultiValueMap duyinMap = new MultiValueMap();
        String chengyu = records.get(0).getChengyu();
        String[] initialDuyin = PinyinHelper.convertToPinyinArray(chengyu.charAt(0), PinyinFormat.WITH_TONE_NUMBER);
        String duyin = PinyinHelper.convertToPinyinString(chengyu, ",", PinyinFormat.WITH_TONE_NUMBER);
        //只有一种读音，直接入库
        if(initialDuyin.length == 1){

        }else{
            //有多种读音，分析数据占比
            for(ChengyuLog log : records){
                Chengyu lastChengyu = chengyuService.getChengyuCache(log.getLastChengyu());
                //上下文成语末尾字读音
                String lastChengyuTailDuyin = lastChengyu.getSpell().split(",")[3];
                //从同音字模式中确定读法(带声调)
                if(log.getType().equals(ChengyuConstant.ChengyuTypeEnum.SAME.getType())){
                    if(ArrayUtils.contains(initialDuyin,lastChengyuTailDuyin)){
                        //该读音使用次数 + 1
                        duyinMap.put(lastChengyuTailDuyin,1);
                    }
                }
                //从形音字中确定拼音(无声调)
                if(log.getType().equals(ChengyuConstant.ChengyuTypeEnum.LIKE.getType())){
                    //去掉声调
                    lastChengyuTailDuyin = lastChengyuTailDuyin.substring(0, lastChengyuTailDuyin.length() - 1);
                    //去掉声调后相同拼音的使用次数 + 1
                    for(String temp : initialDuyin){
                        String pinyin = temp.substring(0, temp.length() - 1);
                        if(pinyin.equals(lastChengyuTailDuyin)){
                            duyinMap.put(temp,1);
                        }
                    }
                }
            }
            //从数据中选取使用次数最多的读音
            String finalInitialDuyin = null;
            int count = 0;
            Iterator it = duyinMap.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry e =(Map.Entry) it.next();
                List<Integer> temp = (List<Integer>) e.getValue();
                if(temp.size() > count){
                    finalInitialDuyin = (String) e.getKey();
                    count = temp.size();
                }
            }
            duyin = finalInitialDuyin + duyin.substring(duyin.indexOf(","));
        }
        result = new Chengyu(chengyu, duyin, 2);
        return result;
    }

    public void deleteLog(String key) {
        Example example = new Example(ChengyuLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(CHENGYU, key);
        ChengyuLog update = new ChengyuLog();
        update.setIsDeleted(1);
        mapper.updateByExampleSelective(update, example);
    }
}
