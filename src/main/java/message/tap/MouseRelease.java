package message.tap;

import message.DescribeHeader;
import message.TapMessageProcess;

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
