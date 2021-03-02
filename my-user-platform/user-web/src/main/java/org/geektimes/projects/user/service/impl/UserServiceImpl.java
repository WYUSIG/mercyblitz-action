package org.geektimes.projects.user.service.impl;

import org.geektimes.projects.user.domain.ResultDto;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.DatabaseUserRepository;
import org.geektimes.projects.user.repository.UserRepository;
import org.geektimes.projects.user.service.UserService;

import java.util.Collection;

/**
 * 用户服务 {@link UserService} 实现类
 **/
public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new DatabaseUserRepository();

    @Override
    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public ResultDto getByNameAndPassword(String userNam, String password) {
        User user = userRepository.getByNameAndPassword(userNam, password);
        if (user != null) {
            return new ResultDto(200,"登录成功！");
        } else {
            return new ResultDto(500, "登录失败！");
        }
    }

    @Override
    public ResultDto save(User user) {
        boolean result = userRepository.save(user);
        if(result == true) {
            return new ResultDto(200,"注册成功！");
        } else {
            return new ResultDto(500,"注册失败！电话或邮箱已经被注册过！");
        }
    }
}
