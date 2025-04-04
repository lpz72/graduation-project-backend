package org.lpz.graduationprojectbackend.model.request;

import lombok.Data;

import java.util.List;

/**
 * 用户标签更新请求体
 */
@Data
public class UpdateTagsRequest {

    private List<String> tagsNameList;
}
