package cn.lsz.gongzhonghao.hajimiemasidie.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;

/**
 * 没配上不同业务输出不同日志，
 * 只好自己做个低配版，
 * 用于记录那些未被识别的成语以便后续补充
 * 早期版本，现已舍弃
 * 
 * @author LSZ 2020/03/17 15:33
 * @contact 648748030@qq.com
 */
@Component
public class MyLogUtils {

/*    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static FileOutputStream fileOutputStream;

    @PostConstruct
    public void init() {
        try {
            //File file = new File("src\\main\\resources\\log\\chengyu.log");
            File file = new File("/project/hajimiemasidie/chengyu.log");
            if(file.exists()){
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void info(String msg){
        StringBuilder sb=new StringBuilder(msg);
        sb.append("\n");
        try {
            fileOutputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestory(){
        LOGGER.debug("释放自定义Log资源");
        try {
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
