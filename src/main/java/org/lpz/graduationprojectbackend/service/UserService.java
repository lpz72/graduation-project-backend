package org.lpz.graduationprojectbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lpz.graduationprojectbackend.model.domain.Elderhealth;
import org.lpz.graduationprojectbackend.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务接口
 * @author lpz
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     * @param userAccount 账户
     * @param userPassword 密码
     * @param checkPassword 校验密码
     * @param userRole 用户角色
     * @param register
     * @return 用户id
     */
    long userRegister (String userAccount,String userPassword,String checkPassword,Integer userRole,int register);

    /**
     * 用户登录
     *
     * @param userAccount  账户
     * @param userPassword 密码
     * @param request 请求体
     * @return 脱敏用户信息
     */
    User userLogin(String userAccount, String userPassword, int userRole,HttpServletRequest request);

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    User getSavetyUser(User user);

    /**
     * 用户注销
     * @param request
     */
    int userLogout(HttpServletRequest request);
    /**
     * 是否为管理员
     * @param request 获取登录态
     * @return
     */
     boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);

    /**
     * 更新用户信息
     * @param user
     * @param loginUser
     * @return
     */
    int updateUser(User user, User loginUser);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
     User getLoginUser(HttpServletRequest request);


    /**
     * 根据用户角色类别查询用户
     * @param type
     * @return
     */
    List<User> allUsersByType(int type);

    /**
     * 根据身份证号查询用户
     * @param idNumber
     * @return
     */
    User getUserByIdNumber(String idNumber);

    /**
     * 重置用户密码
     * @param id
     * @return
     */
    int resetPassword(long id);

    /**
     * 查询所有的注册申请(管理员、护士、医生)
     * @return
     */
    List<User> applyList();

    /**
     * 同意注册申请
     * @param userId
     * @return
     */
    Integer applyConfirm(long userId);
}
