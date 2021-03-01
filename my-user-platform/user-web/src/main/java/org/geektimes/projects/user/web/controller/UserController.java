package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.impl.UserServiceImpl;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.util.Collection;

/**
 * 用户领域控制层
 *
 * @since 1.0
 **/
@Path("/user")
public class UserController implements RestController {

    private UserService userService = new UserServiceImpl();


    @Path("/getAll")
    public Collection<User> getAll(HttpServletRequest request, HttpServletResponse response){
        return userService.getAll();
    }
}
