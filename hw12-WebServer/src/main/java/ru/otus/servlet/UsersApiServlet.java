package ru.otus.servlet;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.hibernate.dao.UserDaoHibernate;

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

//    private long extractIdFromRequest(HttpServletRequest request) {
//        String[] path = request.getPathInfo().split("/");
//        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
//        return Long.parseLong(id);
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        var userString = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        User user = gson.fromJson(jb.toString(), User.class);
        userDao.insertOrUpdate(user);

//        resp.setStatus(HttpServletResponse.SC_CREATED);
        // https://www.logicbig.com/tutorials/java-ee-tutorial/java-servlet/servlet-redirect.html
//        resp.setStatus(308);
//        resp.setHeader("Location", "http//localhost:8080/users");

//        resp.sendRedirect("http//localhost:8080/users");
//        response.setStatus(307);
//        response.addHeader("Location", "<url>");
//        resp.setStatus(307);
//        resp.addHeader("Location", "http//localhost:8080/users");
//        resp.setContentType("text/html");
        resp.getOutputStream().print(gson.toJson(user));
        resp.setStatus(HttpServletResponse.SC_OK);

//
//        String encodedData = req.getQueryString();
//
//        long id = Long.getLong(req.getParameter("id"));

//        try {
//            Gson gson = new Gson();
//            User user = (User)gson.fromJson(jb.toString(), User.class);
//        } catch (Exception e) {
//            throw new IOException("Error parsing JSON request string");
//        }

//        long id = Long.getLong(req.getParameter("id"));
//        String name = req.getParameter("username");
//        int age = Integer.decode(req.getParameter("age"));
//        String login = req.getParameter("login");
//        String password = req.getParameter("password");

//        User user = new User(id, name, age, login, password);

//        userDao.insertOrUpdate(user);

//        resp.sendRedirect(USERS_PATH);
        // request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
