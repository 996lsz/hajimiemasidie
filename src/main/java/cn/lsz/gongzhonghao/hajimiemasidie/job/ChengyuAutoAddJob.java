package cn.lsz.gongzhonghao.hajimiemasidie.job;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.Chengyu;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuAutoAdd;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuLog;
import cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu.ChengyuAutoAddService;
import cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu.ChengyuLogService;
import cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu.ChengyuService;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 公众号定时任务-自动收录新成语
 * （该JOB是用于持续收录新成语的方法，但是觉得正确性得不到保障然后停用了）
 * 
 * @author LSZ 2020/03/18 10:08
 * @contact 648748030@qq.com
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class ChengyuAutoAddJob {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChengyuLogService chengyuLogService;

    @Autowired
    private ChengyuService chengyuService;

    @Autowired
    private ChengyuAutoAddService chengyuAutoAddService;

    /**
     * 每天检查新增提及的成语
     */
    //@Scheduled(cron = "0 0 3 * * ?")
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void addChengyuJob() throws Exception {
        //查询提及数量 > 2的成语， 作为新增成语
        MultiValueMap multiValueMap = chengyuLogService.selectMentionChengyu();
        Iterator it = multiValueMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry e =(Map.Entry) it.next();
            //根据规则，选取最大概率读音的一个保存入库
            Chengyu chengyu = chengyuLogService.decideInitial((List<ChengyuLog>) e.getValue());
            try {
                chengyu = chengyuService.saveOrUpdate(chengyu);
                chengyuAutoAddService.save(chengyu);
                chengyuLogService.deleteLog((String) e.getKey());
            }catch (Exception ex){
                chengyuService.remove(chengyu);
                LOGGER.error(ExceptionUtils.getStackTrace(ex));
            }
        }
    }

    /**
     * 每5分钟检查chengyu_auto_add表中是否有需要更新的数据
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void deletedChengyuJob() {
        ChengyuAutoAdd query = new ChengyuAutoAdd(ChengyuAutoAdd.UpdateFlagEnum.TO_BE_DELETED);
        List<ChengyuAutoAdd> chengyuAutoAddList = chengyuAutoAddService.select(query);
        for(ChengyuAutoAdd auto : chengyuAutoAddList){
            chengyuService.remove(auto.getChengyu());
            auto.setUpdateFlag(ChengyuAutoAdd.UpdateFlagEnum.DELETED.getType());
            chengyuAutoAddService.updateByPrimaryKeySelective(auto);
        }
    }

    /**
     * 每5分钟检查chengyu_auto_add表中是否有需要更新的数据
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void updateChengyuJob() {
        ChengyuAutoAdd query = new ChengyuAutoAdd(ChengyuAutoAdd.UpdateFlagEnum.COMFIRM);
        List<ChengyuAutoAdd> chengyuAutoAddList = chengyuAutoAddService.select(query);
        for(ChengyuAutoAdd auto : chengyuAutoAddList){
            //这里注意有autoID与chengyuID公用的情况,导致保存chengyu时使用了autoID，所以autoID要做处理
            Integer id = auto.getId();
            auto.setId(null);
            chengyuService.saveOrUpdate(auto);
            auto.setId(id);
            auto.setUpdateFlag(ChengyuAutoAdd.UpdateFlagEnum.SUCCESS.getType());
            chengyuAutoAddService.updateByPrimaryKeySelective(auto);
        }
    }

}
