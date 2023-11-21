package message.client2client.tap;

import message.client2client.DescribeHeader;
import message.client2client.TapMessageProcess;

public class MousePress extends TapMessageProcess {
    @Override
    protected char getType() {
        return DescribeHeader.Mouse_Press;
    }

    @Override
    protected void performAction(int keyCode) {
        robot.mousePress(keyCode);
    }
}
