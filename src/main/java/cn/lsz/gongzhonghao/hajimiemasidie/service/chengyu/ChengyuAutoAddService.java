package cn.lsz.gongzhonghao.hajimiemasidie.service.chengyu;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.Chengyu;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuAutoAdd;
import cn.lsz.gongzhonghao.hajimiemasidie.service.ProxySelf;
import core.service.MyBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * description
 * 
 * @author LSZ 2020/03/15 10:14
 * @contact 648748030@qq.com
 */
@Service
@Transactional
public class ChengyuAutoAddService extends MyBaseService<ChengyuAutoAdd> implements ProxySelf<ChengyuAutoAddService> {

    public void save(Chengyu chengyu) {
        ChengyuAutoAdd autoAdd  = new ChengyuAutoAdd(chengyu);
        autoAdd.setCreateDate(new Date());
        mapper.insertSelective(autoAdd);
    }
}
