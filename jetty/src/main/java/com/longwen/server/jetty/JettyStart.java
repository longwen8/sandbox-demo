package com.longwen.server.jetty;

import com.longwen.server.CoreServer;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * Jetty Server的测试启动类
 */

public class JettyStart {

    public static void premain(String args, Instrumentation inst){
        CoreServer jetty = new JettyCoreServer();
        try {
            jetty.bind(CoreConfigure.toConfigure("",""),inst);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        System.out.println("jetty start");

    }
}
