package cn.iecas.message.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("AirMessageInstanceInfo")
public class AirmessageInstanceInfo implements Serializable {

    private static final long serialVersionUID = -4728042755820173508L;

    @ApiModelProperty(value = "用户ID", required = true)
    private Integer userId;

    @ApiModelProperty(value = "用户名字")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty(value = "实例ID")
    private Integer id;

    @ApiModelProperty(value = "消息ID", required = true)
    private Integer messageId;

    @ApiModelProperty(value = "创建实例时间")
    private Date pullTime;

    @ApiModelProperty(value = "是否阅读", required = true)
    private Boolean read;

    @ApiModelProperty(value = "是否删除", required = true)
    private Boolean deleted;

    @ApiModelProperty(value = "消息详情", hidden = true)
    @TableField(exist = false)
    private AirmessageTemplateInfo messageInfo;

    public AirmessageInstanceInfo(Integer userId, Integer messageId, Date pullTime) {
        this.userId = userId;
        this.messageId = messageId;
        this.pullTime = pullTime;
        this.read = false;
        this.deleted = false;
    }

    public AirmessageInstanceInfo(Integer messageId, Integer userId) {
        this.userId = userId;
        this.messageId = messageId;
    }

    public AirmessageInstanceInfo(Integer messageId) {
        this.messageId = messageId;
    }
}
