package cn.iecas.message.utils.resultjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * 返回结果信息属性封装类
 */
@Data
public class ResponseBody implements Serializable {

    private static final long serialVersionUID = 2964938196968968404L;

    private String code;

    private String message;

    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
    private Map<String, Object> data;

    public ResponseBody() {
        super();
    }

    public ResponseBody(String code, String message, Object...data) {
        super();
        this.code = code;
        this.message = message;
        this.data = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            this.data.put(String.valueOf(i), data[i]);
        }
    }

    /**
     * 构造函数：默认成功或失败，无数据
     * @param resultCode
     */
    public ResponseBody(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /**
     * 构造函数：枚举获取结果以及数据
     * @param resultCode
     * @param data
     */
    public ResponseBody(ResultCode resultCode, Object...data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            this.data.put(String.valueOf(i), data[i]);
        }
    }

    /**
     * 构造函数：枚举获取结果以及数据，可指定JSONkey
     * @param resultCode
     * @param data
     */
    public ResponseBody(ResultCode resultCode, Object[] key, Object... data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            this.data.put((String) key[i], data[i]);
        }
    }

    /**
     * 构造函数：枚举获取NULL结果，可指定JSONkey
     * @param resultCode
     * @param data
     */
    public ResponseBody(ResultCode resultCode, String key, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = new HashMap<>();
        this.data.put(key, data);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
