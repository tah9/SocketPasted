package message;


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
    public void send(DataSocket targetDso, Integer encodeKeyCode) {
        targetDso.writeChar(DescribeHeader.Mouse_Move);
        targetDso.writeInt(encodeKeyCode);
        targetDso.flush();
    }


    @Override
    public void receive(DataSocket dis) {

        int lParam = dis.readInt(); // 接收这个int值
        int x = lParam & 0xFFFF; // 从这个int值中解码出x坐标
        int y = lParam >> 16; // 从这个int值中解码出y坐标

        // 获取当前鼠标的位置
        Point currentMousePosition = MouseInfo.getPointerInfo().getLocation();

        // 计算新的鼠标位置
        int newX = currentMousePosition.x + x;
        int newY = currentMousePosition.y + y;

        // 移动鼠标到新的位置
        robot.mouseMove(newX, newY);

    }

    @Override
    public Integer getData(DataSocket dso) {
        return dso.readInt();
    }
}
