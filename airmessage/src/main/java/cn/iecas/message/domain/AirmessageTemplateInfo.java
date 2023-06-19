package cn.iecas.message.domain;

import cn.iecas.message.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("MessageInfo")
@EnableConfigurationProperties({AirmessageTemplateInfo.class})
@ConfigurationProperties(prefix = "template")
public class AirmessageTemplateInfo implements Serializable {

    private static final long serialVersionUID = -5828195155180016225L;

    private Integer userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户真实名称
     */
    private String realName;

    /**
     * ID
     */
    private Integer id;

    /**
     * 发送消息的服务ID required : true
     */
    private Integer serviceId;

    /**
     * 创建消息用户ID
     */
    private Integer sourceUserId;

    /**
     * 消息等级数字标识 required : true
     */
    private Integer notificationLevel;

    /**
     * 消息描述
     */
    private String notificationLevelDescrip;

    /**
     * 消息等级文本标识 required : true
     */
    private String notificationType;

    /**
     * 消息标题 required : true
     */
    private String notificationTitle;

    /**
     * 消息内容 required : true
     */
    private String notificationContent;

    /**
     * 目标用户级别 required : true
     */
    private String targetUserLevel;

    /**
     * 通知的目标用户ID
     */
    private Integer targetUserId;

    /**
     * 消息创建时间
     */
    private Timestamp createTime;

    /**
     * 消息有效时间 required : true
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp invalidatedTime = new Timestamp(DateUtils.getAffterDate(7).getTime());

    /**
     * 是否创建公告 required : true
     */
    private boolean proclamation;

    /**
     * 消息实例 对应多个实例时使用
     */
    private List<AirmessageInstanceInfo> airmessageInstanceInfos;

    /**
     * 消息实例 对应一个实例时使用
     */
    private AirmessageInstanceInfo AirMessageInstanceInfo;

    /**
     * 统计信息
     */
    private int msgTotal;

    private int pulledOut;

    private int readedNum;

    private int deletedNum;

    private String  startTime;

    public String getNotificationType() {
        this.setNotificationType();
        return this.notificationType;
    }

    public void setNotificationType() {
        if (this.notificationLevel == 1) {
            this.notificationType = "普通";
        } else if (this.notificationLevel == 2) {
            this.notificationType = "警告";
        } else if (this.notificationLevel == 3) {
            this.notificationType = "严重";
        } else if (this.notificationLevel == 4) {
            this.notificationType = "致命";
        } else {
            this.notificationType = String.valueOf(this.notificationLevel);
        }
    }
}
