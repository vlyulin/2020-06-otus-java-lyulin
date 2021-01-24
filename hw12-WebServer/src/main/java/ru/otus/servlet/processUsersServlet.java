package ru.otus.servlet;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class processUsersServlet extends HttpServlet {

    private static final String PROCESS_USERS_PAGE_TEMPLATE = "process_user.html";
    private static final String TEMPLATE_ATTR_USER = "user";
    private static final int ID_PATH_PARAM_POSITION = 1;

    private final UserDao userDao;
    private final TemplateProcessor templateProcessor;

    public processUsersServlet(TemplateProcessor templateProcessor, UserDao userDao) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        User user = new User();
        long id = ServletHelpers.extractIdFromRequest(req, ID_PATH_PARAM_POSITION);
        if( id != -1 && id != 0) {
            user = userDao.findById(id).orElse(null);
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_USER, user);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(PROCESS_USERS_PAGE_TEMPLATE, paramsMap));
    }
}
