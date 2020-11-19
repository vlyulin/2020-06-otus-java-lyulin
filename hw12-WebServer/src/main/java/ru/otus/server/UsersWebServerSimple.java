package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.core.dao.UserDao;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.CORSFilter;
import ru.otus.servlet.UsersApiServlet;
import ru.otus.servlet.UsersServlet;
import ru.otus.servlet.processUsersServlet;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class UsersWebServerSimple implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    public static final String USERS_PATH = "/users";
    public static final String API_USER_PATH = "/api/user/*";
    public static final String PROCESS_USER_PATH = "/process_user/*";

    private final UserDao userDao;
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public UsersWebServerSimple(int port, UserDao userDao, Gson gson, TemplateProcessor templateProcessor) {
        this.userDao = userDao;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user/*", "/process_user/*"));

        // Anti CORS
        FilterHolder holder = new FilterHolder(new CORSFilter());
        servletContextHandler.addFilter(holder, "/*", EnumSet.of(DispatcherType.REQUEST));

        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(templateProcessor, userDao)), USERS_PATH);
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(userDao, gson)), API_USER_PATH);
        servletContextHandler.addServlet(new ServletHolder(new processUsersServlet(templateProcessor, userDao)), PROCESS_USER_PATH);
        return servletContextHandler;
    }
}
