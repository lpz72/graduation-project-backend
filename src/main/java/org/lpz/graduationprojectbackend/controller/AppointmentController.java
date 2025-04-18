package org.lpz.graduationprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lpz.graduationprojectbackend.common.BaseResponse;
import org.lpz.graduationprojectbackend.common.ErrorCode;
import org.lpz.graduationprojectbackend.common.ResultUtils;
import org.lpz.graduationprojectbackend.exception.BusinessException;
import org.lpz.graduationprojectbackend.model.domain.Appointment;
import org.lpz.graduationprojectbackend.model.domain.News;
import org.lpz.graduationprojectbackend.model.domain.Schedule;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.service.AppointmentService;
import org.lpz.graduationprojectbackend.service.ElderhealthService;
import org.lpz.graduationprojectbackend.service.NewsService;
import org.lpz.graduationprojectbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@Slf4j
public class AppointmentController {

    @Resource
    private AppointmentService appointmentService;

    /**
     * 获取对应科室的所有医生
     * @param department
     * @return
     */
    @GetMapping("/doctors")
    public BaseResponse<List<User>> getDoctors(String department){

        if (department == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<User> list = appointmentService.getDoctors(department);
        if (list == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(list);
    }


    @GetMapping("/schedule")
    public BaseResponse<List<Schedule>> getSchedule(String department,String dateTime,Integer type) {
        if (StringUtils.isAnyBlank(department,dateTime) || (type != 0 && type != 1)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Schedule> schedules = appointmentService.getSchedule(department,dateTime,type);
        if (schedules == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(schedules);

    }

    /**
     * 获取医生或护士的排班信息
     * @param userId
     * @return
     */
    @GetMapping("/schedule/worker")
    public BaseResponse<List<Schedule>> getScheduleOfWorker(long userId) {
        if (userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Schedule> schedules = appointmentService.getScheduleOfWorker(userId);
        if (schedules == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(schedules);

    }

    /**
     * 根据日期查询该医生或护士的排班信息
     * @param userId
     * @param time
     * @return
     */
    @GetMapping("/schedule/worker/byDate")
    public BaseResponse<List<Schedule>> getScheduleOfWorker(long userId,String time) {
        if (time == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Schedule> schedules = appointmentService.getScheduleOfWorker(userId,time);
        if (schedules == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(schedules);

    }


    /**
     * 添加预约信息
     * @param appointment
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Integer> add(@RequestBody Appointment appointment) {
        if (appointment == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        int result = appointmentService.add(appointment);
        return ResultUtils.success(result);
    }

    /**
     * 查询该用户所有的预约记录
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Appointment>> getAppointments(long userId) {
        if (userId < 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        List<Appointment> list = appointmentService.getAppointments(userId);
        if (list == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(list);
    }

    /**
     * 取消预约，更新type
     * @param id
     * @return
     */
    @GetMapping("/cancel")
    public BaseResponse<Integer> cancelAppointment(long id) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        int i = appointmentService.cancelAppointment(id);
        return ResultUtils.success(i);
    }

    /**
     * 获取当前值班的并且结束时间在当前时间之后的医生的排班信息
     * @return
     */
    @GetMapping("/emergency")
    public BaseResponse<List<Schedule>> emergency() {

        List<Schedule> emergency = appointmentService.emergency();
        if (emergency == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(emergency);

    }



}
