package message.client2client.tap;

import message.client2client.DescribeHeader;
import message.client2client.TapMessageProcess;

public class MouseRelease extends TapMessageProcess {
    @Override
    protected char getType() {
        return DescribeHeader.Mouse_Release;
    }

    @Override
    protected void performAction(int keyCode) {
        robot.mouseRelease(keyCode);
    }
}
