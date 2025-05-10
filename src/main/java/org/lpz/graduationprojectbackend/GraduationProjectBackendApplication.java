package org.lpz.graduationprojectbackend;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.lpz.graduationprojectbackend.mapper")
@EnableScheduling // 启用定时任务
public class GraduationProjectBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraduationProjectBackendApplication.class, args);
    }

}
