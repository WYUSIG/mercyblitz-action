package org.geektimes.projects.user.web.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 字符编码 Filter
 *
 * @since 1.0
 */
public class CharsetEncodingFilter implements Filter {

    private String encoding = null;

    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
        this.servletContext = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpRequest.setCharacterEncoding(encoding);
            httpResponse.setCharacterEncoding(encoding);
//            servletContext.log("设置编码为UTF-8");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
