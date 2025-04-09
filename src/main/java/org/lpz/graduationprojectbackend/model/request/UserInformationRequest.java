package org.lpz.graduationprojectbackend.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserInformationRequest implements Serializable {
    private Long userId;
    private Long id;
    private Integer weight;
    private Integer height;
    private Integer highBloodPressure;
    private Integer lowBloodPressure;
    private Integer heartRate;
    private Integer leftEye;
    private Integer rightEye;
    private Integer hypertension;
    private Integer diabetes;
    private Integer heartDisease;
    private Integer stroke;
    private Integer cancer;
    private Integer osteoporosis;
    private String otherDiseases;
    private Integer isDelete;
    private Date createTime;
}
