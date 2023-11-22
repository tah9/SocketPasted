package message.toclient.tap;

import message.DescribeHeader;
import message.toclient.TapMessageProcess;

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
