//package org.lpz.graduationprojectbackend.job;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import lombok.extern.slf4j.Slf4j;
//import org.lpz.usercenter.model.domain.User;
//import org.lpz.usercenter.service.UserService;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Slf4j
//public class PreCacheJob {
//
//    @Resource
//    private UserService userService;
//
//    @Resource
//    private RedisTemplate<String,Object> redisTemplate;
//
//    @Resource
//    private RedissonClient redissonClient;
//
//    //重点用户
//    List<Long> mainUserList = Arrays.asList(1L);
//
//    //每天执行，预热推荐用户
//    //表示在每天的00:25分定时执行
//    @Scheduled(cron = "0 25 0 * * *")
//    public void doCacheRecommendUser(){
//        RLock rLock = redissonClient.getLock("yupao:precachejob:docache:lock");
//        try {
//            //只有一个线程能获得锁
//            if (rLock.tryLock(0,-1,TimeUnit.MILLISECONDS)){
//                for (Long userId : mainUserList) {
//                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//                    Page<User> userPage = userService.page(new Page<>(1,20),queryWrapper);
//                    String key = String.format("yupao:user:recommend:%s",userId);
//                    ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
//                    //写缓存
//                    try {
//                        valueOperations.set(key,userPage,30000, TimeUnit.MILLISECONDS);
//                    } catch (Exception e) {
//                        log.error("redis set key error",e);
//                    }
//                }
//            }
//
//        } catch (InterruptedException e) {
//            log.error("doCacheRecommendUser error",e);
//        }finally {
//            //只能释放自己的锁
//            if (rLock.isHeldByCurrentThread()){
//                rLock.unlock();
//            }
//        }
//
//    }
//
//
//}
