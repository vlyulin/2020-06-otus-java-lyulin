package ru.otus.frontend.servlet;

import javax.servlet.http.HttpServletRequest;

public class ServletHelpers {
    public static long extractIdFromRequest(HttpServletRequest request, int IdPathParamPosition) {
        String id = String.valueOf(- 1);

        try {
            String[] path = request.getPathInfo().split("/");
            id = (path.length > 1)? path[IdPathParamPosition]: String.valueOf(- 1);
        } catch (Exception e) {
        }
        return Long.parseLong(id);
    }
}
