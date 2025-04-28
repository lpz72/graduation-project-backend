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
import org.lpz.graduationprojectbackend.model.request.*;
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
        int register = userRegisterRequest.getRegister();
        int userRole = userRegisterRequest.getUserRole();

        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        long id = userService.userRegister(userAccount,userPassword,checkPassword,userRole,register);
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


    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request){
        if (!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (userDeleteRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean b = userService.removeById(userDeleteRequest.getId());//已经配置过逻辑删除，所以mybatis-plus会自动改为逻辑删除
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

    /**
     * 查询所有的注册申请(管理员、护士、医生)
     * @param request
     * @return
     */
    @GetMapping("/list/apply")
    public BaseResponse<List<User>> applyList(HttpServletRequest request){
        if (!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        List<User> list = userService.applyList();
        if (list == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(list);
    }

    /**
     * 同意注册申请
     * @param userId
     * @return
     */
    @GetMapping("/apply/confirm")
    public BaseResponse<Integer> applyConfirm(long userId){
        if (userId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(userService.applyConfirm(userId));
    }

    /**
     * 根据角色类别查询用户
     * @param type
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<User>> allUsersByType(int type,HttpServletRequest request){
        if (type < 0 || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        List<User> list = userService.allUsersByType(type);
        if (list == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (user.getUserRole() != 0) {
            list = list.stream().map(user1 -> userService.getSavetyUser(user1)).collect(Collectors.toList());
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

    @GetMapping("/reset")
    public BaseResponse<Integer> resetPassword(long id) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        int i = userService.resetPassword(id);

        return ResultUtils.success(i);

    }



}
