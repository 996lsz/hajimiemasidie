package cn.lsz.gongzhonghao.hajimiemasidie;

import cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant;
import cn.lsz.gongzhonghao.hajimiemasidie.entity.*;
import cn.lsz.gongzhonghao.hajimiemasidie.util.MathUtils;
import cn.lsz.gongzhonghao.hajimiemasidie.util.MenuUtils;
import cn.lsz.gongzhonghao.hajimiemasidie.util.XmlBeanUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cn.lsz.gongzhonghao.hajimiemasidie.constant.AppConstant.TOKEN_URL;

/**
 * description
 * 
 * @author LSZ 2020/02/13 15:01
 * @contact 648748030@qq.com
 */
public class CommonTest {



}
