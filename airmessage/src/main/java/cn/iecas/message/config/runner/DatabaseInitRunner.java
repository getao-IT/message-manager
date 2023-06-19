package cn.iecas.message.config.runner;

import cn.iecas.message.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class DatabaseInitRunner implements ApplicationRunner {

    @Autowired
    DatabaseService databaseService;

    @Override
    public void run(ApplicationArguments arguments){
        databaseService.initDatabase();
    }



}
