package com.longwen.server.util;

import org.apache.commons.io.IOUtils;

import java.net.InetAddress;
import java.net.Socket;

/**
 * 网络通讯工具类
 */
public class NetworkUtils {

    /***
     * 测试主机Host的port端口是否被使用
     * @param host 指定IP
     * @param port 指定端口
     * @return TRUE:端口已经被占用;FALSE:端口尚未被占用
     */
    public static boolean isPortInUsing(String host, int port) {
        Socket socket = null;
        try {
            final InetAddress Address = InetAddress.getByName(host);
            socket = new Socket(Address, port);  //建立一个Socket连接
            return socket.isConnected();
        } catch (Throwable cause) {
            // ignore
        } finally {
            if (null != socket) {
                IOUtils.closeQuietly(socket);
            }
        }
        return false;
    }

}
