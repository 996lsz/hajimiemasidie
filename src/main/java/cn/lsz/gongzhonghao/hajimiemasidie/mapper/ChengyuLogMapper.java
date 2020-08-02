package cn.lsz.gongzhonghao.hajimiemasidie.mapper;

import cn.lsz.gongzhonghao.hajimiemasidie.entity.ChengyuLog;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * desc
 *
 * @author LSZ 2020/03/15 14:06
 * @contact 648748030@qq.com
 */
public interface ChengyuLogMapper extends Mapper<ChengyuLog> {

    @Select("SELECT * FROM chengyu_log WHERE chengyu IN ( SELECT db.chengyu FROM ( SELECT DISTINCT chengyu, create_by FROM chengyu_log WHERE is_deleted = 0 ) db GROUP BY chengyu HAVING count(*) >= 2 )")
    List<ChengyuLog> selectMentionChengyu();
}

