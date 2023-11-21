package ui.kit;

import socket.ClientMachine;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class PlatformKits {
    /*
    使用对象的哈希值作为ID
     */
    public static ClientMachine createMachine(String socketId) throws UnknownHostException {
        // 获取默认工具包
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // 获取屏幕分辨率
        Dimension screenSize = toolkit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        ClientMachine machine = new ClientMachine(
                InetAddress.getLocalHost().getHostName(),
                System.getProperty("os.name"),
                width, height,
                socketId);
        return machine;
    }



}
