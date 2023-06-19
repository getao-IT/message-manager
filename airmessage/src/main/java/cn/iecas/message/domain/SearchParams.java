package cn.iecas.message.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@ApiModel("SearchParams")
public class SearchParams implements Serializable {

    private static final long serialVersionUID = -7981059887329882778L;

    /**
     * 各种id
     */
    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 服务id
     */
    private int service_id;

    /**
     * 源用户id
     */
    private int source_user_id;

    /**
     * 通知级别
     */
    private int notification_level;

    /**
     * 通知类型
     */
    private String notification_type;

    /**
     * 通知标题
     */
    private String notification_title;

    /**
     * 通知标题
     */
    private String notification_content;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 排序属性
     */
    private String[] orders;

    /**
     * 分页属性
     */
    private int pageNum = 1;

    private int pageSize = 10;

    /**
     * 显示数量
     */
    private int showNum = 4;

    /**
     * 是否是管理员
     */
    private boolean is_admin;

    /**
     * 用户等级
     */
    private int adminLevel;

    /**
     * 批量传参使用
     */
    private Integer[] paramsArr;

    /**
     * 时间参数
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 消息统计维度
     */
    private String standard;

    /**
     * 是否获取公告 required : true
     */
    private boolean proclamation;

}
