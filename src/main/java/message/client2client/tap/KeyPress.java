package message.client2client.tap;

import message.client2client.DescribeHeader;
import message.client2client.TapMessageProcess;

public class KeyPress extends TapMessageProcess {
    @Override
    protected char getType() {
        return DescribeHeader.Key_Press;
    }

    @Override
    protected void performAction(int keyCode) {
        robot.keyPress(keyCode);
    }


}
