package core;

import lombok.Data;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Version;
import java.util.Date;

/**
 * description
 * 
 * @author LSZ 2019/10/29 10:20
 * @contact 648748030@qq.com
 */
@Data
public class BaseInfo {

    public static final String CREATE_DATE = "creatieDate";
    public static final String CREATE_BY = "createBy";
    public static final String LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String LAST_UPDATE_BY = "lastUpdateBy";
    public static final String ROW_VERSION_NUMBER = "rowVersionNumber";

    public static final String ASC = " asc";
    public static final String DESC = " desc";

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 最后更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 版本号
     */
    @Version
    private Integer rowVersionNumber;


}
