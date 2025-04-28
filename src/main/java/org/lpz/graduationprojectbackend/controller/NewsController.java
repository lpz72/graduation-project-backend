package org.lpz.graduationprojectbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lpz.graduationprojectbackend.common.BaseResponse;
import org.lpz.graduationprojectbackend.common.ErrorCode;
import org.lpz.graduationprojectbackend.common.ResultUtils;
import org.lpz.graduationprojectbackend.exception.BusinessException;
import org.lpz.graduationprojectbackend.model.domain.Elderhealth;
import org.lpz.graduationprojectbackend.model.domain.News;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.model.request.*;
import org.lpz.graduationprojectbackend.service.ElderhealthService;
import org.lpz.graduationprojectbackend.service.NewsService;
import org.lpz.graduationprojectbackend.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.lpz.graduationprojectbackend.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/news")
@Slf4j
public class NewsController {

    @Resource
    private UserService userService;
    @Resource
    private ElderhealthService elderhealthService;
    @Resource
    private NewsService newsService;

    /**
     * 获取所有资讯
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<News>> getNews(HttpServletRequest request){
//        if (request == null){
//            throw new BusinessException(ErrorCode.NO_AUTH);
//        }
//
//        User loginUser = userService.getLoginUser(request);
//        if (loginUser == null) {
//            throw new BusinessException(ErrorCode.NO_AUTH);
//        }

        List<News> news = newsService.getNews();
        if (news == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(news);
    }


    /**
     * 阅读次数+1
     * @param id
     * @return
     */
    @GetMapping("/addReadCount")
    public BaseResponse<Integer> addReadCount(Integer id) {
        if(id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(newsService.addReadCount(id));

    }

    /**
     * 添加一个资讯
     * @param news
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addNews(@RequestBody News news) {
        if(news == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(newsService.save(news));

    }

    /**
     * 删除一个资讯
     * @param newDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteNew(@RequestBody NewDeleteRequest newDeleteRequest, HttpServletRequest request){
        if (!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (newDeleteRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean b = newsService.removeById(newDeleteRequest.getId());//已经配置过逻辑删除，所以mybatis-plus会自动改为逻辑删除
        return ResultUtils.success(b);

    }

    /**
     * 更新一个资讯
     * @param news
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody News news){
        //检验数据是否为空
        if (news == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean b = newsService.updateById(news);
        return ResultUtils.success(b);
    }


}
