package cn.lsz.gongzhonghao.hajimiemasidie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * description
 * 
 * @author LSZ 2020/08/02 17:29
 * @contact 648748030@qq.com
 */
@Data
public class WxToken {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_in")
	private Integer expiresIn;

	private Integer errcode;

	private String errmsg;
}
