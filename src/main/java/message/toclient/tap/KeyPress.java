package message.toclient.tap;

import message.DescribeHeader;
import message.toclient.TapMessageProcess;

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
