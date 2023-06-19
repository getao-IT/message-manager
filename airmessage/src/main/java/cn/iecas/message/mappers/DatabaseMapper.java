package cn.iecas.message.mappers;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DatabaseMapper {
    List<String>  selectTableNames();
    List<String> selectViewNames();
    void createDatasetImageInfo();

}
