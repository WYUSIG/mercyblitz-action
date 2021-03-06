package org.geektimes.projects.user.service;

import org.geektimes.projects.user.domain.ResultDto;
import org.geektimes.projects.user.domain.User;

import java.util.Collection;

/**
 * 用户服务
 *
 * @since 1.0
 */
public interface UserService {

    Collection<User> getAll();

    ResultDto getByNameAndPassword(String userNam, String password);

    ResultDto save(User user);
}
