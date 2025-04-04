package org.lpz.graduationprojectbackend.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis") //与application.yml相关联
@Data
public class RedissonConfig {

    //字段名须一致
    private String port;
    private String host;

    @Bean
   public RedissonClient redissonClient(){
        //1.创建配置
        Config config = new Config();
        String redissonAddress = String.format("redis://%s:%s",host,port);
        config.useSingleServer().setAddress(redissonAddress).setDatabase(3);
        //2.创建实例
        RedissonClient redisson = Redisson.create(config);

        return redisson;
   }
}
