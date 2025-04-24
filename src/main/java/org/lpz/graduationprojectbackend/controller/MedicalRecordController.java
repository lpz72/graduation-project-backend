package org.lpz.graduationprojectbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.lpz.graduationprojectbackend.common.BaseResponse;
import org.lpz.graduationprojectbackend.common.ErrorCode;
import org.lpz.graduationprojectbackend.common.ResultUtils;
import org.lpz.graduationprojectbackend.exception.BusinessException;
import org.lpz.graduationprojectbackend.model.domain.Medicalrecord;
import org.lpz.graduationprojectbackend.model.domain.News;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.service.ElderhealthService;
import org.lpz.graduationprojectbackend.service.MedicalrecordService;
import org.lpz.graduationprojectbackend.service.NewsService;
import org.lpz.graduationprojectbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/medical")
@Slf4j
public class MedicalRecordController {

    @Resource
    private UserService userService;
    @Resource
    private MedicalrecordService medicalrecordService;

    /**
     * 获取所有就诊记录
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Medicalrecord>> getRecords(long userId, HttpServletRequest request){

        if (userId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        List<Medicalrecord> records = medicalrecordService.getRecords(userId);
        if (records == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(records);
    }

    /**
     * 获取该护士所有的护理记录
     * @param nurseId
     * @return
     */
    @GetMapping("/nurse")
    public BaseResponse<List<Medicalrecord>> getRecordsOfNurse(long nurseId){

        if (nurseId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Medicalrecord> records = medicalrecordService.getRecordsOfNurse(nurseId);
        if (records == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(records);
    }

    /**
     * 根据日期获取该护士所有的护理记录
     * @param nurseId
     * @return
     */
    @GetMapping("/nurse/byDate")
    public BaseResponse<List<Medicalrecord>> getRecordsOfNurse(long nurseId,String time){

        if (nurseId < 0 || time == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Medicalrecord> records = medicalrecordService.getRecordsOfNurse(nurseId,time);
        if (records == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(records);
    }

    /**
     * 获取该医生所有的诊断记录
     * @param doctorId
     * @return
     */
    @GetMapping("/doctor")
    public BaseResponse<List<Medicalrecord>> getRecordsOfDoctor(long doctorId){

        if (doctorId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Medicalrecord> records = medicalrecordService.getRecordsOfDoctor(doctorId);
        if (records == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(records);
    }

    /**
     * 根据日期获取该医生所有的诊断记录
     * @param doctorId
     * @return
     */
    @GetMapping("/doctor/byDate")
    public BaseResponse<List<Medicalrecord>> getRecordsOfDoctor(long doctorId,String time){

        if (doctorId < 0 || time == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<Medicalrecord> records = medicalrecordService.getRecordsOfDoctor(doctorId,time);
        if (records == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(records);
    }

    @PostMapping("/add")
    public BaseResponse<Integer> addRecord(@RequestBody Medicalrecord medicalrecord) {
        if (medicalrecord == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        int i = medicalrecordService.addRecord(medicalrecord);
        return ResultUtils.success(i);

    }

    /**
     * 增加或修改就诊评价
     * @param id
     * @param comment
     * @return
     */
    @GetMapping("/comment")
    public BaseResponse<Integer> addComment(long id,String comment) {
        if (comment == null || id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        int i = medicalrecordService.addComment(id,comment);
        return ResultUtils.success(i);
    }

    /**
     * 根据预约信息的id查询是否已有该预约信息的就诊记录
     * @param appointmentId
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<Medicalrecord> searchByAppointmentId(long appointmentId) {
        if (appointmentId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Medicalrecord medicalrecord = medicalrecordService.searchByAppointmentId(appointmentId);
        return ResultUtils.success(medicalrecord);
    }




}
