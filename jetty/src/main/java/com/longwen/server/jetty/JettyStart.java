package com.longwen.server.jetty;

import com.longwen.server.CoreServer;
import java.io.IOException;

/**
 * Jetty Server的测试启动类
 */

public class JettyStart {

    public static void main(String[] args) {
        CoreServer jetty = new JettyCoreServer();
        try {
            jetty.bind(CoreConfigure.toConfigure("",""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
