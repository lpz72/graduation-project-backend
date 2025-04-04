package org.lpz.graduationprojectbackend.model.request;


import lombok.Data;

/**
 * 用户加入队伍请求体
 */
@Data
public class TeamJoinRequest {


    /**
     * 队伍id
     */
    private Long teamId;


    /**
     * 密码
     */
    private String password;

}
