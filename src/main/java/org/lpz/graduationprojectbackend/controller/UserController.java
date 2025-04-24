package org.lpz.graduationprojectbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lpz.graduationprojectbackend.common.BaseResponse;
import org.lpz.graduationprojectbackend.common.ErrorCode;
import org.lpz.graduationprojectbackend.common.ResultUtils;
import org.lpz.graduationprojectbackend.exception.BusinessException;
import org.lpz.graduationprojectbackend.model.domain.Elderhealth;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.model.request.UpdateTagsRequest;
import org.lpz.graduationprojectbackend.model.request.UserInformationRequest;
import org.lpz.graduationprojectbackend.model.request.UserLoginRequest;
import org.lpz.graduationprojectbackend.model.request.UserRegisterRequest;
import org.lpz.graduationprojectbackend.service.ElderhealthService;
import org.lpz.graduationprojectbackend.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.lpz.graduationprojectbackend.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = {"http://localhost:3000"},allowCredentials = "true")
//@CrossOrigin(origins = {"http://39.107.143.21:80"},allowCredentials = "true")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ElderhealthService elderhealthService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){

        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        int userRole = userRegisterRequest.getUserRole();

        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        long id = userService.userRegister(userAccount,userPassword,checkPassword,userRole);
        return ResultUtils.success(id);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){

        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        int userRole = userLoginRequest.getUserRole();

        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, userRole,request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){

        if (request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int i = userService.userLogout(request);
        return ResultUtils.success(i);
    }

    @GetMapping("/information")
    public BaseResponse<Elderhealth> userInformation(Integer userId) {
        if(userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(elderhealthService.getInformation(userId));

    }

    /**
     * 获取当前用户信息
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object object = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) object;
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // todo 校验用户是否合法
        long id = user.getId();
        User user1 = userService.getById(id);
        User savetyUser = userService.getSavetyUser(user1);
        return ResultUtils.success(savetyUser);
    }

    /**
     * 获取根据id脱敏的信息
     * @param userId
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<User> getUser(Integer userId) {
        if (userId < 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        User user = userService.getById(userId);
        user = userService.getSavetyUser(user);
        return ResultUtils.success(user);

    }

//    /**
//     * 搜索所有用户
//     * @param username
//     * @param request
//     * @return
//     */
//    @GetMapping("/search")
//    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
//        //HttpServletRequest request是获取用户登录态
//        // 鉴权，仅管理员可查询
//        if (!userService.isAdmin(request)){
//            throw new BusinessException(ErrorCode.NO_AUTH);
//        }
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        if (StringUtils.isNotBlank(username)){
//            queryWrapper.like("username",username);
//        }
//
//        List<User> userList = userService.list(queryWrapper);
//        List<User> collect = userList.stream().map(user -> userService.getSavetyUser(user)).collect(Collectors.toList());
//        return ResultUtils.success(collect);
//
//    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){
        if (!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean b = userService.removeById(id);//已经配置过逻辑删除，所以mybatis-plus会自动改为逻辑删除
        return ResultUtils.success(b);

    }

    @PostMapping("/update")
    //因为前端的请求是json数据类型，所以需要使用@RequestBody注解，前提是post方式才会生效
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request){
        //检验数据是否为空
        if (user == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User loginUSer = userService.getLoginUser(request);
        int result = userService.updateUser(user, loginUSer);
        return ResultUtils.success(result);
    }
    @PostMapping("/update/information")
    //因为前端的请求是json数据类型，所以需要使用@RequestBody注解，前提是post方式才会生效
    public BaseResponse<Integer> updateUserInformation(@RequestBody UserInformationRequest userInformationRequest, HttpServletRequest request){
        //检验数据是否为空
        if (userInformationRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

//        User loginUSer = userService.getLoginUser(request);
//        int result = userService.updateUser(user, loginUSer);
        int i = elderhealthService.updateInformation(userInformationRequest);
        return ResultUtils.success(i);
    }

    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        //使用页数存储key
        String key = String.format("yupao:user:recommend:%d",pageNum);
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();

        //如果有缓存，直接读缓存
        Page<User> userPage = (Page<User>) valueOperations.get(key);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }

        //无缓存，查数据库，设置缓存
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //使用分页
         userPage = userService.page(new Page<>(pageNum,pageSize),queryWrapper);
        try {
            valueOperations.set(key,userPage,30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error",e);
        }
        return ResultUtils.success(userPage);
    }

    /**
     * 根据角色类别查询用户
     * @param type
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<User>> allUsersByType(int type){
        if (type < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<User> list = userService.allUsersByType(type);
        if (list == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(list);
    }

    /**
     * 根据身份证号查询用户
     * @param idNumber
     * @return
     */
    @GetMapping("/list/byIdNumber")
    public BaseResponse<User> getUserByIdNumber(String idNumber) {
        if (idNumber == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getUserByIdNumber(idNumber);

        return ResultUtils.success(user);

    }



}
