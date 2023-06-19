package cn.iecas.message.utils.resultjson;

/**
 * 返回状态信息枚举类
 */
public enum ResultCode {

    SUCCESS("0", "success"),
    FAIL("18", "查询数据失败!"),
    CREATE_MESSAGE_FAIL("180001", "创建新消息通知失败，请检查创建数据！"),
    DELETE_FAIL("180002", "删除消息或实例通知失败！"),
    DELETE_NULL_FAIL("180002", "删除失败，不存在该数据！"),
    GET_NULL_FAIL("180003", "查询为空！"),
    UPDATE_FAIL("180004", "更新消息通知失败！"),
    PERMISSION_FAIL("180005", "没有该权限!"),
    DATA_INVALID_FAIL("180006", "时间数据有效性校验失败!");


    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
