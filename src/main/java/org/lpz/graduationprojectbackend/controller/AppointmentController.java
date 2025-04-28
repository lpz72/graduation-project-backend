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
import org.lpz.graduationprojectbackend.model.request.ScheduleDeleteRequest;
import org.lpz.graduationprojectbackend.model.request.UserDeleteRequest;
import org.lpz.graduationprojectbackend.service.*;
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

    @Resource
    private ScheduleService scheduleService;

    @Resource
    private UserService userService;

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
    public BaseResponse<List<Schedule>> getScheduleOfWorkerByDate(long userId,String time) {
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
     * 取消预约，更新type，对应排班信息的可预约人数+1
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


    /**
     * 根据id获取该医生或护士所有的预约信息
     * @param workerId
     * @return
     */
    @GetMapping("/worker")
    public BaseResponse<List<Appointment>> getAppointmentOfWorker(long workerId) {
        if (workerId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Appointment> appointment = appointmentService.getAppointmentOfWorker(workerId);
        if (appointment == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(appointment);
    }

    /**
     * 根据id和时间获取该医生或护士所有的预约信息
     * @param workerId
     * @param time
     * @return
     */
    @GetMapping("/worker/byDate")
    public BaseResponse<List<Appointment>> getAppointmentOfWorkerByDate(long workerId,String time) {
        if (workerId < 0 || time == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Appointment> appointment = appointmentService.getAppointmentOfWorker(workerId,time);
        if (appointment == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(appointment);
    }

    /**
     * 添加排班信息
     * @param schedule
     * @return
     */
    @PostMapping("/schedule/add")
    public BaseResponse<Integer> addSchedule(@RequestBody Schedule schedule) {
        if (schedule == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        boolean i = scheduleService.save(schedule);
        return ResultUtils.success(i ? 1 : 0);
    }

    /**
     * 修改排班信息
     * @param schedule
     * @return
     */
    @PostMapping("/schedule/update")
    public BaseResponse<Integer> updateSchedule(@RequestBody Schedule schedule) {
        if (schedule == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        boolean i = scheduleService.updateById(schedule);
        return ResultUtils.success(i ? 1 : 0);
    }


    @PostMapping("/schedule/delete")
    public BaseResponse<Boolean> deleteSchedule(@RequestBody ScheduleDeleteRequest scheduleDeleteRequest, HttpServletRequest request){
        if (!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (scheduleDeleteRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean b = scheduleService.removeById(scheduleDeleteRequest.getId());//已经配置过逻辑删除，所以mybatis-plus会自动改为逻辑删除
        return ResultUtils.success(b);

    }


}
