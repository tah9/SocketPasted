package message.tap;

import message.DescribeHeader;
import message.TapMessageProcess;

public class KeyRelease extends TapMessageProcess {
    @Override
    protected char getType() {
        return DescribeHeader.Key_Release;
    }

    @Override
    protected void performAction(int keyCode) {
        robot.keyRelease(keyCode);
    }
}
