package message.toclient;


import message.DescribeHeader;
import socket.DataSocket;

import java.awt.*;


public class MouseMoveMessageProcess implements MessageProcess<Integer> {

    private final Robot robot;

    public MouseMoveMessageProcess() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendToClient(DataSocket targetDso, Integer encodeKeyCode) {
        targetDso.writeChar(DescribeHeader.Mouse_Move);
        targetDso.writeInt(encodeKeyCode);
        targetDso.flush();
    }


    @Override
    public void clientProcess(DataSocket dso) {

        int lParam = dso.readInt(); // 接收这个int值
        int x = lParam & 0xFFFF; // 从这个int值中解码出x坐标
        int y = lParam >> 16; // 从这个int值中解码出y坐标


        System.out.println(x + "," + y);
        // 移动鼠标到新的位置
        robot.mouseMove(x, y);

    }

    @Override
    public Integer transferGetData(DataSocket dso) {
        return dso.readInt();
    }
}
