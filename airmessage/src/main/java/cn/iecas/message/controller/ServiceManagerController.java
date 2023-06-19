package cn.iecas.message.controller;

import cn.iecas.message.service.ConnectService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/receive")
public class ServiceManagerController {


    @Autowired
    private ConnectService serviceManager;

    @GetMapping(value = "/service")
    public JSONObject getService(int service_id) {
        JSONObject service = serviceManager.getService(service_id);
        return service;
    }
}
