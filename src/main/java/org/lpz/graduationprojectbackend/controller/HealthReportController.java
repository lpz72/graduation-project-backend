package org.lpz.graduationprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.*;

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
     * 根据身份证号获取所有体检记录
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<HealthReportQuery>> getHealthRecords(String idNumber,HttpServletRequest request){

        if (idNumber == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (request == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        List<HealthReportQuery> records = healthreportService.getHealthRecords(idNumber);
        if (records == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(records);
    }


    /**
     * 增加一个体检报告
     * @param healthreport
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Integer> addReport(@RequestBody Healthreport healthreport) {
        if(healthreport == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(healthreportService.addReport(healthreport));

    }

    @GetMapping("/search")
    public BaseResponse<List<Healthreport>> searchReport(String username,String idNumber) {
        if (StringUtils.isAnyBlank(username,idNumber)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Healthreport> list = healthreportService.searchReport(username, idNumber);

        return ResultUtils.success(list);

    }


}
