package org.lpz.graduationprojectbackend.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

/**
 * 体检报告查询封装类（返回给前端的）
 */
@Data
public class HealthReportQuery {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 病人id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 性别
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 电话
     */
    private String phone;

    /**
     * 左眼视力
     */
    private Double leftEye;

    /**
     * 右眼视力
     */
    private Double rightEye;

    /**
     * 色觉
     */
    private String colorVision;

    /**
     * 左耳听力（m）
     */
    private Integer leftEar;

    /**
     * 右耳听力（m）
     */
    private Integer rightEar;

    /**
     * 嗅觉
     */
    private String smell;

    /**
     * 口吃
     */
    private String stuttering;

    /**
     * 面部
     */
    private String face;

    /**
     * 心率（次/分钟）
     */
    private Integer heartRate;

    /**
     * 呼吸频率（次/分钟）
     */
    private Integer breathingRate;

    /**
     * 体温（℃）
     */
    private Double temperature;

    /**
     * 血压高压（mmHg）
     */
    private Integer highBloodPressure;

    /**
     * 血压低压（mmHg）
     */
    private Integer lowBloodPressure;

    /**
     * 肝
     */
    private String liver;

    /**
     * 脾
     */
    private String spleen;

    /**
     * 肺
     */
    private String lung;

    /**
     * 身高（cm）
     */
    private Integer height;

    /**
     * 体重（kg）
     */
    private Integer weight;

    /**
     * 皮肤
     */
    private String skin;

    /**
     * 四肢
     */
    private String limbs;

    /**
     * 甲状腺
     */
    private String thyroidGland;

    /**
     * 健康状况（0：优 1：良 2：中 3：差）
     */
    private Integer healthCondition;

    /**
     * 健康建议
     */
    private String healthAdvice;

    /**
     * 下次体检建议日期
     */
    private Date nextTime;

    /**
     * 体检日期
     */
    private Date createTime;

    /**
     * 体检机构
     */
    private String institution;
    /**
     * 体检医生
     */
    private String doctor;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;
}
