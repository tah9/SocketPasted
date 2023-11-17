package message.tap;

import message.DescribeHeader;
import message.TapMessageProcess;

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
