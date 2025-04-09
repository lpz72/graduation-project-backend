package org.lpz.graduationprojectbackend;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.lpz.graduationprojectbackend.model.domain.User;
import org.lpz.graduationprojectbackend.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserTest {

    @Resource
    private UserService userService;

    @Test
    void InsertUser() {

        User user = new User();
        user.setId(0L);
        user.setUsername("李鹏振");
        user.setIdNumber("41148120030727552");
        user.setUserAccount("");
        user.setAvatarUrl("");
        user.setUserPassword("");
        user.setGender(0);
        user.setAge(0);
        user.setPhone("");
        user.setEmail("");
        user.setUserStatus(0);
        user.setDepartment("");
        user.setPosition("");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        user.setUserRole(0);





    }

}
