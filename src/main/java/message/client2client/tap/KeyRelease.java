package message.client2client.tap;

import message.client2client.DescribeHeader;
import message.client2client.TapMessageProcess;

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
