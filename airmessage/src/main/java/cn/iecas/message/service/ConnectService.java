package cn.iecas.message.service;

import cn.aircas.utils.file.FileUtils;
import cn.iecas.message.domain.ServiceManager;
import cn.iecas.message.utils.resultjson.ResultCode;
import cn.iecas.message.utils.resultjson.ResultGenerator;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashSet;


@Slf4j
@Service
public class ConnectService {

    @Autowired
    private ResultGenerator resultGenerator;

    @Value("${dir.rootPath}")
    private String rootPath;

    /**
     * 用于连接到服务
     * @param service_id
     * @return
     */
    public JSONObject getService(int service_id) {
        //String path = FileUtils.getStringPath(this.rootPath, "swagger.yaml");
        String path = "C:\\Users\\dell\\Desktop\\application.yml";
        // 获取文件内容
        String yamlContent = "";
        try {
            InputStream inputStream = new FileInputStream(path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            yamlContent = outputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException异常：{} ", e.getMessage());
        } catch (IOException e) {
            log.error("IOException异常：{} ", e.getMessage());
        }
        ServiceManager.serviceId = service_id;
        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"yaml_content"}, yamlContent);
    }
}