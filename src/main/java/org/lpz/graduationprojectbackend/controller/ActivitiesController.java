package org.lpz.graduationprojectbackend.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.lpz.graduationprojectbackend.common.BaseResponse;
import org.lpz.graduationprojectbackend.common.ErrorCode;
import org.lpz.graduationprojectbackend.common.ResultUtils;
import org.lpz.graduationprojectbackend.exception.BusinessException;
import org.lpz.graduationprojectbackend.model.domain.Activity;
import org.lpz.graduationprojectbackend.model.domain.Joinactivities;
import org.lpz.graduationprojectbackend.model.domain.News;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/activities")
@Slf4j
public class ActivitiesController {

    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;
    @Resource
    private JoinactivitiesService joinactivitiesService;

    /**
     * 获取所有活动
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Activity>> getActivities(HttpServletRequest request){

        if (request == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        List<Activity> activities = activityService.getActivities();
        if (activities == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(activities);
    }


    @GetMapping("/add")
    public BaseResponse<Integer> addActivity(long userId,long activityId,Integer type) {
        if(userId < 0 || activityId < 0 || type == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        int i = joinactivitiesService.addActivity(userId, activityId, type);

        return ResultUtils.success(i);

    }

    @GetMapping("/history")
    public BaseResponse<List<Joinactivities>> hasJoin(long userId) {
        if (userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Joinactivities> list = joinactivitiesService.hasJoin(userId);
        if (list == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(list);
    }

    /**
     * 取消预约，更新type
     * @param activityId
     * @return
     */
    @GetMapping("/cancel")
    public BaseResponse<Integer> cancelJoin(long activityId) {
        if (activityId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        int i = joinactivitiesService.cancelJoin(activityId);
        return ResultUtils.success(i);
    }


}
