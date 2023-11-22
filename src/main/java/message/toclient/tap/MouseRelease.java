package message.toclient.tap;

import message.DescribeHeader;
import message.toclient.TapMessageProcess;

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
