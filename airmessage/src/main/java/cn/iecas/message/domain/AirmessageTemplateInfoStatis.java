package cn.iecas.message.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirmessageTemplateInfoStatis implements Serializable {

    /**
     * 统计信息
     */
    private int msgTotal = 0;

    private int pulledOut = 0;

    private int readedNum = 0;

    private int deletedNum = 0;

    private Integer notificationLevel;

    private String notificationType;

    private String proportion = "0%";

    public String getNotificationType() {
        this.setNotificationType();
        return this.notificationType;
    }

    public AirmessageTemplateInfoStatis(Integer notificationLevel, String notificationType) {
        this.notificationLevel = notificationLevel;
        this.notificationType = notificationType;
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
