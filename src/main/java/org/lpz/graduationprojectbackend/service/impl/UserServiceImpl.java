package org.lpz.graduationprojectbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lpz.graduationprojectbackend.common.ErrorCode;
import org.lpz.graduationprojectbackend.exception.BusinessException;
import org.lpz.graduationprojectbackend.mapper.ElderhealthMapper;
import org.lpz.graduationprojectbackend.mapper.UserMapper;
import org.lpz.graduationprojectbackend.model.domain.Elderhealth;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.lpz.graduationprojectbackend.constant.UserConstant.ADMIN_ROLE;
import static org.lpz.graduationprojectbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 * @author lpz
 */
@Service
@Slf4j //记录日志
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ElderhealthMapper elderhealthMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String salt = "lpz";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,Integer userRole) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }

        //账户长度不小于4位
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户长度过短");
        }
        //密码长度不小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
        //账户不能包含特殊字符
        String validPattern = "[^a-zA-Z0-9_]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //账户不能重复  涉及到查询数据库，可以把该条判断放到最后，防止资源浪费，避免并不必要的查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);//设置查询条件
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户重复");
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());

        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword); //保存加密后的密码
        user.setUserRole(userRole);
        int result = userMapper.insert(user);
        if (result != 1){
            throw new BusinessException(ErrorCode.INSERT_ERROR);
        }

        //当注册的是老人时，才操作
        if (userRole == 1) {
            QueryWrapper<Elderhealth> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("userId",user.getId());
            Integer i = elderhealthMapper.selectCount(queryWrapper1);
            if(i == 0) {
                Elderhealth elderhealth = new Elderhealth();
                elderhealth.setUserId(user.getId());
                elderhealthMapper.insert(elderhealth);
            }
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, int userRole,HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //账户长度不小于4位
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户长度过短");
        }
        //密码长度不小于8位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
        //账户不能包含特殊字符
        String validPattern = "[^a-zA-Z0-9_]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());

        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);//设置查询条件
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null){
            log.info("user login failed,userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NULL_USER,"用户不存在");
        }
        if(user.getUserRole() != userRole) {
            throw new BusinessException(ErrorCode.NULL_USER,"身份不正确");
        }

        //3.用户脱敏
        User saftyUser = getSavetyUser(user);

        //4.记录用户登录态  Attribute相当于一个map
        request.getSession().setAttribute(USER_LOGIN_STATE,saftyUser);
        return saftyUser;
    }


    /**
     * 用户脱敏
     * @param user
     * @return
     */
    @Override
    public User getSavetyUser(User user){
        if (user == null){
            throw new BusinessException(ErrorCode.NULL_USER,"用户不存在");
        }
        User saftyUser = new User();
        saftyUser.setId(user.getId());
        saftyUser.setUsername(user.getUsername());
        saftyUser.setUserAccount(user.getUserAccount());
        saftyUser.setAvatarUrl(user.getAvatarUrl());
        saftyUser.setGender(user.getGender());
        saftyUser.setPhone(user.getPhone());
        saftyUser.setEmail(user.getEmail());
        saftyUser.setUserRole(user.getUserRole());
        saftyUser.setAge(user.getAge());
        saftyUser.setPosition(user.getPosition());
        saftyUser.setDepartment(user.getDepartment());
        saftyUser.setHospital(user.getHospital());
        saftyUser.setIdNumber(user.getIdNumber());
        saftyUser.setSpecialty(user.getSpecialty());

        return saftyUser;
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //用户退出登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 是否为管理员
     * @param request 获取登录态
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole() != ADMIN_ROLE){
            return false;
        }

        return true;
    }

    @Override
    public boolean isAdmin(User loginUser){
        if (loginUser.getUserRole() != ADMIN_ROLE){
            return false;
        }
        return true;
    }

    @Override
    public int updateUser(User user, User loginUser) {
        long id = user.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //todo 补充校验，如果用户没有任何需要更新的值，就直接报错，不用执行update语句
        //如果是管理员，则允许修改任意用户
        //如果不是管理员，只允许修改当前（自己的）信息
        if (!isAdmin(loginUser) && id != loginUser.getId()){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User oldUser = userMapper.selectById(id);
        if (oldUser == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        //todo 是否添加修改密码功能
//        String userPassword = user.getUserPassword();

//        //密码长度不小于8位
//        if (userPassword.length() < 8) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
//        }
//        //加密
//        String encryptPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
//        user.setUserPassword(encryptPassword);


        return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        return (User) userObj;
    }




}




