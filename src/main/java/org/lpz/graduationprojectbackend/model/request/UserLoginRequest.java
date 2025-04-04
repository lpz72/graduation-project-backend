package org.lpz.graduationprojectbackend.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * 用户登录请求体
 * @author lpz
 */
@Data
public class UserLoginRequest implements Serializable {

    //生成一个序列化id
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private String userAccount;

    private String userPassword;

}
