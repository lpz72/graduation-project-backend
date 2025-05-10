package org.lpz.graduationprojectbackend.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 更新活动请求体
 * @TableName activity
 */
@Data
public class ActivityUpdateRequest {

    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动类型（0：健康讲座 1：义诊 2：体检活动 3：健康交流会）
     */
    private String type;

    /**
     * 活动主题标签
     */
    private List<String> tags;

    /**
     * 最大参与数
     */
    private Integer peopleCount;

    /**
     * 活动地点
     */
    private String position;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 当前已参与人数
     */
    private Integer currentCount;

}