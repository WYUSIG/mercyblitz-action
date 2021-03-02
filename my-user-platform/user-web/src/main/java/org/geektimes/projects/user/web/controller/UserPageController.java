package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.ResultDto;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.impl.UserServiceImpl;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @ClassName UserPageController
 * @Description: TODO
 * @Author 钟显东
 * @Date 2021/3/2 0002
 * @Version V1.0
 **/
@Path("/userPage")
public class UserPageController implements PageController {

    private UserService userService = new UserServiceImpl();

    @GET
    @Path("/loginForm")
    public String loginForm(HttpServletRequest request, HttpServletResponse response) {
        return "login-form.jsp";
    }

    @GET
    @Path("/loginForward")
    public String loginForward(HttpServletRequest request, HttpServletResponse response) {
        String userNam = request.getParameter("userNam");
        String password = request.getParameter("password");
        ResultDto resultDto = userService.getByNameAndPassword(userNam, password);
        if (resultDto.getCode() == 200) {
            return "login-success.jsp";
        } else {
            return "login-failure.jsp";
        }
    }

    @GET
    @Path("/register")
    public String register(HttpServletRequest request, HttpServletResponse response) {
        return "register.jsp";
    }
}
