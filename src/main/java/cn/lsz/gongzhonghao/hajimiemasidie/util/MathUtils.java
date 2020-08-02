package cn.lsz.gongzhonghao.hajimiemasidie.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * description
 * 
 * @author LSZ 2020/02/16 11:11
 * @contact 648748030@qq.com
 */
public class MathUtils {

    public static int randomNextInt(int num){
        Random rand = new Random();
        return rand.nextInt(num);
    }

    /**
     * 随机从originList取出sizeFrom至sizeTo个数据到新数组
     *
     * @originList 数据源
     * @sizeFrom 随机取数区间
     * @sizeTo 随机取数区间
     * @removeFlag 是否从数据源移除取出的数据
     */
    public static List<Object> randomNewList(List originList, int sizeFrom, int sizeTo, boolean removeFlag){
        if(originList == null || originList.size() < sizeFrom){
            throw new IndexOutOfBoundsException();
        }
        List resultList = new ArrayList();
        int size = randomNextInt(sizeTo + 1 - sizeFrom) + sizeFrom;
        Random rand = new Random();
        while(size-- > 0){
            int randomIndex = rand.nextInt(originList.size() - 1);
            resultList.add(originList.get(randomIndex));
            //是否从数据源移除
            if(removeFlag){
                originList.remove(randomIndex);
            }
        }
        return resultList;
    }
}
