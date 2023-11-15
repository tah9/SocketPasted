package message;


import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;


/*
使用json格式传递消息
x:
y:
left:
right:
center:
 */
public class MouseMessageProcess implements MessageProcess {
    @Override
    public void send(DataOutputStream dos, byte[] data) throws IOException {
        dos.writeChar('M');
        dos.writeInt(data.length);
        dos.write(data);
    }

    @Override
    public void receive(byte[] data) {
        String string = new String(data);
        String[] split = string.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
//        System.out.println(x+","+y);
        try {
            Robot robot = new Robot();
            // 获取当前鼠标的位置
            Point currentMousePosition = MouseInfo.getPointerInfo().getLocation();

            // 计算新的鼠标位置
            int newX = currentMousePosition.x + x;
            int newY = currentMousePosition.y + y;

            // 移动鼠标到新的位置
            robot.mouseMove(newX, newY);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}
