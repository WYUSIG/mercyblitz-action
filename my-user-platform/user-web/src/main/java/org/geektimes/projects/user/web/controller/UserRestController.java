package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.ResultDto;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.impl.UserServiceImpl;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.IOException;
import java.util.Collection;

/**
 * 用户领域控制层
 *
 * @since 1.0
 **/
@Path("/user")
public class UserRestController implements RestController {

    private UserService userService = new UserServiceImpl();


    @Path("/getAll")
    public Collection<User> getAll(HttpServletRequest request, HttpServletResponse response) {
        return userService.getAll();
    }

    /**
     * 邮箱/电话 密码 登录
     *
     * @param request
     * @param response
     * @return
     */
    @Path("/signIn")
    public ResultDto signIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userNam = request.getParameter("userNam");
        String password = request.getParameter("password");
        return userService.getByNameAndPassword(userNam, password);
    }

    @POST
    @Path("/signUp")
    public ResultDto save(HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setPassword(password);
        System.out.println(user);
        return userService.save(user);
    }
}
