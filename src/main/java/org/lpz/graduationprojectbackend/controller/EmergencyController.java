package org.lpz.graduationprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lpz.graduationprojectbackend.common.BaseResponse;
import org.lpz.graduationprojectbackend.common.ErrorCode;
import org.lpz.graduationprojectbackend.common.ResultUtils;
import org.lpz.graduationprojectbackend.exception.BusinessException;
import org.lpz.graduationprojectbackend.model.domain.Activity;
import org.lpz.graduationprojectbackend.model.domain.Emergency;
import org.lpz.graduationprojectbackend.model.domain.Joinactivities;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.service.ActivityService;
import org.lpz.graduationprojectbackend.service.EmergencyService;
import org.lpz.graduationprojectbackend.service.JoinactivitiesService;
import org.lpz.graduationprojectbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/emergency")
@Slf4j
public class EmergencyController {

    @Resource
    private UserService userService;
    @Resource
    private EmergencyService emergencyService;

    /**
     * 获取所有求救信息
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Emergency>> getEmergencies(long workerId,HttpServletRequest request){

        if (workerId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (request == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        List<Emergency> emergencies = emergencyService.getEmergencies(workerId);
        if (emergencies == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(emergencies);
    }


    /**
     * 添加一个求救记录
     * @param emergency
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Integer> addEmergency(@RequestBody Emergency emergency) {
        if(emergency == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        int i = emergencyService.addEmergency(emergency);

        return ResultUtils.success(i);

    }





}
