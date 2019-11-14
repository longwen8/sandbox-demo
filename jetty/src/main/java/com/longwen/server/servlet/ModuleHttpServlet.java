package com.longwen.server.servlet;

import com.alibaba.jvm.sandbox.api.http.Http;
import com.longwen.server.app.core.manager.CoreModuleManager;
import com.longwen.server.jetty.CoreConfigure;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ModuleHttpServlet extends HttpServlet{

    private static final String SLASH = "/";

    private final CoreConfigure cfg;

    private final CoreModuleManager coreModuleManager;

    public ModuleHttpServlet(final CoreConfigure cfg,final CoreModuleManager coreModuleManager){
        this.cfg = cfg;
        this.coreModuleManager = coreModuleManager;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMethod(req, resp, Http.Method.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMethod(req, resp, Http.Method.POST);
    }

    private void doMethod(final HttpServletRequest req,
                          final HttpServletResponse resp,
                          final Http.Method expectHttpMethod) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();
        writer.println("Hello Module!");

    }
}
