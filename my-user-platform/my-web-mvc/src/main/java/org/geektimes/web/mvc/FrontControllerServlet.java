package org.geektimes.web.mvc;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.geektimes.web.mvc.controller.Controller;
import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.substringAfter;

/**
 * 全局处理Servlet
 *
 * @since 1.0
 */
public class FrontControllerServlet extends HttpServlet {

    /**
     * 请求路径和 {@link Controller}的映射关系缓存
     */
    private Map<String, Controller> controllerMapping = new HashMap<>();

    /**
     * 请求路径和 {@link HandlerMethodInfo} 映射关系缓存
     */
    private Map<String, HandlerMethodInfo> handlerMethodInfoMapping = new HashMap<>();

    /**
     * 初始化 Servlet
     *
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) {
        initHandleMethods();
    }

    /**
     * 读取所有的 {@link Controller} 的注解元信息 {@link javax.ws.rs.Path}
     * 利用 ServiceLoader 技术（Java SPI）
     */
    private void initHandleMethods() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();
            for (Method method : publicMethods) {
                Set<String> supportedHttpMethods = findSupportedHttpMethod(method);
                Path pathFromMethod = method.getAnnotation(Path.class);
                if (pathFromMethod != null) {
                    requestPath += pathFromMethod.value();
                }
                if (!handlerMethodInfoMapping.containsKey(requestPath)) {
                    handlerMethodInfoMapping.put(requestPath,
                            new HandlerMethodInfo(requestPath, method, supportedHttpMethods));
                    controllerMapping.put(requestPath, controller);
                }
                requestPath = pathFromClass.value();
            }
        }
    }

    /**
     * 获取处理方法中标注的HTTP方法集合
     *
     * @param method
     * @return
     */
    private Set<String> findSupportedHttpMethod(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod httpMethod = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }

        if(supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }

        return supportedHttpMethods;
    }

    /**
     * SCWCD
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // requestURI = /a/hello/world
        String requestURI = request.getRequestURI();
        // servletContextPath = /a or "/" or ""
        String servletContextPath = request.getContextPath();
        String prefixPath = servletContextPath;
        String requestMappingPath = substringAfter(requestURI, StringUtils.replace(prefixPath, "//", "/"));
        Controller controller = controllerMapping.get(requestMappingPath);

        if (controller != null) {

            HandlerMethodInfo handlerMethodInfo = handlerMethodInfoMapping.get(requestMappingPath);
            try {
                if (handlerMethodInfo != null) {
                    String httpMethod = request.getMethod();
                    if (!handlerMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return;
                    }

                    ServletContext servletContext = request.getServletContext();
                    if (controller instanceof PageController) {
//                        PageController pageController = PageController.class.cast(controller);
                        Method handleMethod = handlerMethodInfo.getHandlerMethod();
                        String viewPath = (String) handleMethod.invoke(controller,request,response);
                        if (!viewPath.startsWith("/")) {
                            viewPath = "/" + viewPath;
                        }
                        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                        requestDispatcher.forward(request, response);
                    } else if (controller instanceof RestController) {
                        Method handleMethod = handlerMethodInfo.getHandlerMethod();
                        Object restResult = handleMethod.invoke(controller,request,response);
                        String jsonString = JSON.toJSONString(restResult);
                        response.getWriter().write(jsonString);
                    }
                }
            } catch (IllegalAccessException|InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
