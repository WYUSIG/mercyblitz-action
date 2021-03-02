package org.geektimes.projects.user.web.controller;

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

    @GET
    @Path("/loginForm")
    public String loginForm(HttpServletRequest request, HttpServletResponse response) {
        return "login-form.jsp";
    }

    @GET
    @Path("/register")
    public String register(HttpServletRequest request, HttpServletResponse response) {
        return "register.jsp";
    }
}
