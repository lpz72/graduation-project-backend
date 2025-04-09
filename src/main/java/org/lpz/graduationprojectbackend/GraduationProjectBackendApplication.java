package org.lpz.graduationprojectbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.lpz.graduationprojectbackend.mapper")
public class GraduationProjectBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraduationProjectBackendApplication.class, args);
    }

}
