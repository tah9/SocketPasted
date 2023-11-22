package message.toclient.tap;

import message.DescribeHeader;
import message.toclient.TapMessageProcess;

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
