package org.lpz.graduationprojectbackend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.lpz.graduationprojectbackend.mapper.ScheduleMapper;
import org.lpz.graduationprojectbackend.model.domain.Schedule;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.service.ScheduleService;
import org.lpz.graduationprojectbackend.service.UserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private ScheduleService scheduleService;
    @Resource
    private ScheduleMapper scheduleMapper;

    //秒 分 时 日 月 周
    //每周的周一凌晨执行,更新后面的可预约人数
    /**
     *  0 0 0：表示凌晨 0 点 0 分 0 秒。
     *  ?：表示不指定具体的日期（因为我们已经指定了周几）。
     *  *：表示每个月。
     *  MON：表示周一。
     */

    @Scheduled(cron = "0 0 0 ? * MON")
    public void updateSchedule(){
        List<Schedule> list = scheduleService.list().
                stream().filter(item -> item.getEndTime().after(new Date())).collect(Collectors.toList());
        for (Schedule item : list) {
            item.setPeopleCount(item.getMaxCount());
            scheduleMapper.updateById(item);
        }

    }


}
