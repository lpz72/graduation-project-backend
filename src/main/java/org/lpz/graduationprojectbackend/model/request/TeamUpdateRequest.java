package org.lpz.graduationprojectbackend.model.request;


import lombok.Data;

import java.util.Date;

/**
 * 更新队伍请求体
 */
@Data
public class TeamUpdateRequest {

    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;


    /**
     * 过期时间
     */
    private Date expireTime;


    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

}
