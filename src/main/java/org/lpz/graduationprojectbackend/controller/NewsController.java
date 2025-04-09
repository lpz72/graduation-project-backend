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
import org.lpz.graduationprojectbackend.model.request.UserInformationRequest;
import org.lpz.graduationprojectbackend.model.request.UserLoginRequest;
import org.lpz.graduationprojectbackend.model.request.UserRegisterRequest;
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

        if (request == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        List<News> news = newsService.getNews();
        if (news == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(news);
    }


    @GetMapping("/add")
    public BaseResponse<Integer> addReadCount(Integer id) {
        if(id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(newsService.addReadCount(id));

    }


}
