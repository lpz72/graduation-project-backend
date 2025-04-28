package org.lpz.graduationprojectbackend.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lpz.graduationprojectbackend.common.BaseResponse;
import org.lpz.graduationprojectbackend.common.ErrorCode;
import org.lpz.graduationprojectbackend.common.ResultUtils;
import org.lpz.graduationprojectbackend.exception.BusinessException;
import org.lpz.graduationprojectbackend.model.domain.Activity;
import org.lpz.graduationprojectbackend.model.domain.Joinactivities;
import org.lpz.graduationprojectbackend.model.domain.News;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.model.request.ActivityDeleteRequest;
import org.lpz.graduationprojectbackend.model.request.UserDeleteRequest;
import org.lpz.graduationprojectbackend.service.*;
import org.springframework.web.bind.annotation.*;

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
     * 获取所有结束时间在此之后活动
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Activity>> getActivities(HttpServletRequest request){

//        if (request == null){
//            throw new BusinessException(ErrorCode.NO_AUTH);
//        }
//
//        User loginUser = userService.getLoginUser(request);
//        if (loginUser == null) {
//            throw new BusinessException(ErrorCode.NO_AUTH);
//        }

        List<Activity> activities = activityService.getActivities();
        if (activities == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(activities);
    }


    /**
     * 报名一个活动
     * @param joinactivities
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Integer> joinActivity(@RequestBody Joinactivities joinactivities) {
        if(joinactivities == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        int i = joinactivitiesService.joinActivity(joinactivities);

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
     * @param userId
     * @param activityId
     * @return
     */
    @GetMapping("/cancel")
    public BaseResponse<Integer> cancelJoin(long userId,long activityId) {
        if (activityId < 0 || userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        int i = joinactivitiesService.cancelJoin(userId,activityId);
        return ResultUtils.success(i);
    }

    /**
     * 获取所有活动
     * @param request
     * @return
     */
    @GetMapping("/all")
    public BaseResponse<List<Activity>> getAllActivities(HttpServletRequest request){

        if (request == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        List<Activity> activities = activityService.getAllActivities();
        if (activities == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(activities);
    }


    /**
     * 添加一个活动
     * @param activity
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<Boolean> addActivity(@RequestBody Activity activity) {
        if(activity == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        boolean save = activityService.save(activity);

        return ResultUtils.success(save);

    }

    /**
     * 删除一个活动，并删除已加入的该活动的所有记录
     * @param activityDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Integer> deleteActivity(@RequestBody ActivityDeleteRequest activityDeleteRequest, HttpServletRequest request){
        if (!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (activityDeleteRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        int i = activityService.deleteActivity(activityDeleteRequest.getId());//已经配置过逻辑删除，所以mybatis-plus会自动改为逻辑删除
        return ResultUtils.success(i);

    }

    /**
     * 更新一个活动
     * @param activity
     * @param request
     * @return
     */
    @PostMapping("/update")
    //因为前端的请求是json数据类型，所以需要使用@RequestBody注解，前提是post方式才会生效
    public BaseResponse<Boolean> updateActivity(@RequestBody Activity activity, HttpServletRequest request){
        //检验数据是否为空
        if (activity == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        boolean b = activityService.updateById(activity);
        return ResultUtils.success(b);
    }


}
