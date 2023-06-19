package cn.iecas.message.utils.resultjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONObjectCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.xml.ws.Response;

/**
 * 返回结果方法生成类
 * 其中定义了各种控制器可以调用的结果集处理方法
 * 返回数据类型为JSON
 */
@Component
@Slf4j
public class ResultGenerator {

    /**
     * 默认成功，无数据
     * @return
     */
    public JSONObject getSuccessResult() {
        return JSONObject.parseObject(new ResponseBody(ResultCode.SUCCESS).toString());
    }

    /**
     * 默认成功，有数据
     * @param data
     * @return
     */
    public JSONObject getSuccessResult(Object data) {
        return JSONObject.parseObject(new ResponseBody(ResultCode.SUCCESS, data).toString());
    }

    /**
     * 默认失败，无数据
     * @return
     */
    public JSONObject getFailResult() {
        return JSONObject.parseObject(new ResponseBody(ResultCode.FAIL).toString());
    }

    /**
     * 默认失败，有数据
     * @param message 数据为自定义的错误信息
     * @return
     */
    public JSONObject getFailResult(String message) {
        return JSONObject.parseObject(new ResponseBody(ResultCode.FAIL, message).toString());
    }

    /**
     * 采用枚举中的状态无数据返回结果
     * @param resultCode
     * @return
     */
    public JSONObject getFreeResult(ResultCode resultCode) {
        return JSONObject.parseObject(new ResponseBody(resultCode).toString());
    }

    /**
     * 采用枚举中的状态有数据返回结果
     * @param resultCode
     * @param data 包含多个数据的返回结果
     * @return
     */
    /*public JSONObject getFreeResult(ResultCode resultCode, Object...data) {
        return JSONObject.parseObject(new ResponseBody(resultCode, data).toString());
    }*/

    /**
     * 采用枚举中的状态有数据返回结果，可指定JSONkey
     * @param resultCode
     * @param data 包含多个数据的返回结果
     * @return
     */
    public JSONObject getFreeResult(ResultCode resultCode, Object[] keys, Object...data) {
        return JSONObject.parseObject(new ResponseBody(resultCode, keys, data).toString());
    }

    /**
     * 查询为空的情况返回NULL，可指定JSONkey
     * @param resultCode
     * @param data 包含多个数据的返回结果
     * @return
     */
    public JSONObject getFreeResult(ResultCode resultCode, String keys, Object data) {
        return JSONObject.parseObject(new ResponseBody(resultCode, keys, data).toString());
    }

    /**
     * 自定义返回信息和数据
     * @param code
     * @param message
     * @param data
     * @return
     */
    public JSONObject getFreeResult(String code, String message, Object data) {
        return JSONObject.parseObject(new ResponseBody(code, message, data).toString());
    }

    /**
     * 控制台输出json格式数据
     * @param responseBody
     */
    public void getJsonFormatStr(JSONObject responseBody) {
        String jsonStr = responseBody.toString();
        int level = 0;

        // 存放格式化的json字符串
        StringBuffer jsonFormatStr = new StringBuffer();

        for (int i = 0; i < jsonStr.length() ; i++) {
            // 获取每个字符
            char c = jsonStr.charAt(i);

            // 如果level大于0并且jsonFormatStr中的最后一个字符为\n，jsonFormatStr加入\t
            if (level > 0 && jsonFormatStr.charAt(jsonFormatStr.length() - 1) == '\n') {
                jsonFormatStr.append(getLevelStr(level));
            }

            // 遇到{，[要空格和换行，遇到]，}要渐少空格，遇到‘，’要换行
            switch (c) {
                case '{':
                case '[':
                    jsonFormatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonFormatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonFormatStr.append("\n");
                    level--;
                    jsonFormatStr.append(getLevelStr(level));
                    jsonFormatStr.append(c);
                    break;
                default:
                    jsonFormatStr.append(c);
                    break;
            }
        }
        log.info("========================api/v1/airmessage/message==============================");
        log.info("\n" + jsonFormatStr.toString());
    }

    /**
     * 追加每行数据前的空格，换行后调用
     */
    private String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int lev = 0; lev < level; lev++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

}
