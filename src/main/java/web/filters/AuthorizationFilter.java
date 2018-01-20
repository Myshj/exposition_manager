package web.filters;

import models.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.managers.resource.IResourceManager;
import utils.managers.resource.ResourceBundleAccessor;
import utils.managers.resource.ResourceManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(
//        filterName = "AuthorizationFilter",
//        urlPatterns = {"/admin/*"}
//)
public class AuthorizationFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(AuthorizationFilter.class);

    private final IResourceManager urlManager = new ResourceManager(
            new ResourceBundleAccessor().withResource("urls")
    ).withKey("loginJsp");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init");
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        logger.info("begin filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        System.out.println(role);

        if (unauthorized(role)) {
            logger.info("user unauthorized");
            response.sendRedirect(urlManager.get());
            logger.info("redirected to login service");
            return;
        }

        logger.info("end filter");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean unauthorized(UserRole role) {
        return role == null || !role.getHasAccessToAdminSite().getValue();
    }

    @Override
    public void destroy() {
        logger.info("destroy");
    }
}
