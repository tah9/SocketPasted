package message;

import ui.TransFormB2I;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.DataOutputStream;
import java.io.IOException;

public class KeyMessageProcess implements MessageProcess {
    @Override
    public void send(DataOutputStream dos, byte[] data) throws IOException {
        dos.writeChar('K');
        dos.writeInt(data.length);
        dos.write(data);
    }

    @Override
    public void receive(byte[] data) {
        try {
            Robot robot = new Robot();
            int keyCode = TransFormB2I.bytesToInt(data, 0);
            System.out.println("接收到的 ");
            if (keyCode < 1024) {
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            } else if (keyCode == InputEvent.BUTTON1_DOWN_MASK) {
//                InputEvent.BUTTON1_DOWN_MASK
                robot.mousePress(keyCode);
                robot.delay(100);
                robot.mouseRelease(keyCode);

            } else if (keyCode == InputEvent.BUTTON3_DOWN_MASK) {
                robot.mousePress(keyCode);
                robot.delay(100);
                robot.mouseRelease(keyCode);
            }

        } catch (AWTException e) {
//            throw new RuntimeException(e);
        }
    }

}
