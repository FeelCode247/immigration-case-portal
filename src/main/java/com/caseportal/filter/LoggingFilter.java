package com.caseportal.filter;

import com.caseportal.dao.AuditDAO;
import com.caseportal.model.AuditLog;
import com.caseportal.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        chain.doFilter(request, response);
        try {
            User user = (User) req.getSession().getAttribute("user");
            AuditLog a = new AuditLog();
            a.setUserId(user == null ? null : user.getId());
            a.setAction("REQUEST");
            a.setDetails(req.getMethod() + " " + req.getRequestURI());
            a.setIpAddress(req.getRemoteAddr());
            new AuditDAO().log(a);
        } catch (Exception ignored) {}
    }
}
