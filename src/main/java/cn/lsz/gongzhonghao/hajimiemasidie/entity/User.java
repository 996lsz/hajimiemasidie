package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * description
 * 
 * @author LSZ 2020/02/13 13:44
 * @contact 648748030@qq.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    /*
    * openid等同xml中的userName
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String openid;

    private Date createTime;
}
