package org.lpz.graduationprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.lpz.graduationprojectbackend.common.BaseResponse;
import org.lpz.graduationprojectbackend.common.ErrorCode;
import org.lpz.graduationprojectbackend.common.ResultUtils;
import org.lpz.graduationprojectbackend.exception.BusinessException;
import org.lpz.graduationprojectbackend.model.domain.Healthreport;
import org.lpz.graduationprojectbackend.model.domain.News;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.model.dto.HealthReportQuery;
import org.lpz.graduationprojectbackend.service.ElderhealthService;
import org.lpz.graduationprojectbackend.service.HealthreportService;
import org.lpz.graduationprojectbackend.service.NewsService;
import org.lpz.graduationprojectbackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/health")
@Slf4j
public class HealthReportController {

    @Resource
    private UserService userService;
    @Resource
    private HealthreportService healthreportService;

    /**
     * 获取所有体检记录
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<HealthReportQuery>> getHealthRecords(long userId,HttpServletRequest request){

        if (userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (request == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        List<HealthReportQuery> records = healthreportService.getHealthRecords(userId);
        if (records == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(records);
    }


//    @GetMapping("/add")
//    public BaseResponse<Integer> addReadCount(Integer id) {
//        if(id == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//
//        return ResultUtils.success(newsService.addReadCount(id));
//
//    }


}
