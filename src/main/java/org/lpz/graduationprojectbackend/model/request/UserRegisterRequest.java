package org.lpz.graduationprojectbackend.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * 用户注册请求体
 * @author lpz
 */
@Data
public class UserRegisterRequest implements Serializable {

    //生成一个序列化id
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
