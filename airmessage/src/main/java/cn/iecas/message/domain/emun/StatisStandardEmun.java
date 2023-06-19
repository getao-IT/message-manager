package cn.iecas.message.domain.emun;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统计消息的时间维度枚举类
 */
@AllArgsConstructor
public enum StatisStandardEmun {

    DAY("0"),
    WEEK("1"),
    MONTH("2"),
    YEAR("3");

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
