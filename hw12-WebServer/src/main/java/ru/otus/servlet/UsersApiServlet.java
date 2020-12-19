package ru.otus.servlet;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class UsersApiServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UsersApiServlet.class);

    private static final int ID_PATH_PARAM_POSITION = 1;
    public static final String USERS_PATH = "/users";

    private final UserDao userDao;
    private final Gson gson;

    public UsersApiServlet(UserDao userDao, Gson gson) {
        this.userDao = userDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userDao.findById(ServletHelpers.extractIdFromRequest(request, ID_PATH_PARAM_POSITION)).orElse(null);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            /*report an error*/
            throw new ServletException(e);
        }

        User user = gson.fromJson(jb.toString(), User.class);
        userDao.insertOrUpdate(user);

        resp.getOutputStream().print(gson.toJson(user));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
